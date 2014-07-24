$(document).on({
    ajaxStart: function() { $("#loading_progress").show(); },
    ajaxStop: function() { $("#loading_progress").hide(); }    
});

$('.weekday').click(function () {
    $('#moodCategories').removeClass('hide');
    $('#productCategories').addClass('hide');
    $('#products').addClass('hide');
    $('.moodCategoryID').each(function () {
        $(this).prop('checked', false);
    });
    $('.productCategoryID').each(function () {
        $(this).prop('checked', false);
    });
});
$('.moodCategoryID').click(function () {
    $('#productCategories').removeClass('hide');
    $('#products').addClass('hide');
    $('.productCategoryID').each(function () {
        $(this).prop('checked', false);
    });
});
$('.productCategoryID').click(function () {
    $('#products').removeClass('hide');
    $('#productlist').html('<tr><th>Name</th><th>Volume</th><th>Price</th><th>LCBO ID</th><th></th></tr>');

    showProducts();
});

function showProducts()
{
    weekday = $("input:radio[name ='weekday']:checked").val();
    mood_category_id = $("input:radio[name ='mood_category_id']:checked").val();
    product_category_id = $("input:radio[name ='product_category_id']:checked").val();
    $.ajax({
        url: 'getProductsForRecommend.php',
        type: 'POST',
        data: {
            'weekday' : weekday,
            'mood_category_id' : mood_category_id,
            'product_category_id' : product_category_id
        } 
    }).done(function (data) {
        for (i=0; i<data.result.length; i++)
        {
            $('#productlist').append('<tr class="' + (isInList(data.result[i].recommend_category_id) ? "inlist" : "") +'"><td><a class="listProductName">' + data.result[i].name + '</a></td><td>' + data.result[i].volume + '  ml</td><td>' + formatPrice(data.result[i].price) + '</td><td>' + data.result[i].lcbo_product_id +  '</td><td><input type="hidden" value="' + data.result[i].product_id + '">' + showAction(data.result[i].recommend_category_id) +'</td></tr>');
        }
        bindAction();
    });
}

function formatPrice(input)
{
    return '$' + (input/100);
}

function isInList(input)
{
    if (!input)
        return false;
    else
        return true;
}

function showAction(input)
{
    if (!input)
    {
        return '<a class="ToggleProduct">Add</a>';
    }
    else
    {
        return '<a class="ToggleProduct">Remove</a>';
    }
}

function bindAction()
{
    
    $('.ToggleProduct').unbind('click').click(function () {
        toggleProduct($(this));
    });
    $('.listProductName').unbind('click').click(function () {
        showEditForm($(this));
    });
}

function showEditForm(element)
{
    product_id = $(element).parent().parent().find('input[type=hidden]').val();
    inlist = $(element).parent().parent().hasClass('inlist');
    $.ajax({
        url: 'getProduct.php',
        type: 'POST',
        data: {'product_id': product_id}
    }).done(function (data) {
        if (data.Product)
        {
            $('#editProductContainer').foundation('reveal', 'open'); 
            $('#product_id_e').val(data.Product[0].id);
            $('#lcbo_product_id_e').val(data.Product[0].lcbo_product_id);
            $('#product_name_e').val(data.Product[0].name);
            $('#product_volume_e').val(data.Product[0].volume);
            $('#product_price_e').val(data.Product[0].price);
            $('#product_imageURL_e').val(data.Product[0].imageURL);
            $('#product_inlist_e').val(inlist);
        }
    });
    console.log(product_id);
}

function toggleProduct(element)
{
    product_id = $(element).parent().find('input[type=hidden]').val();
    weekday = $("input:radio[name ='weekday']:checked").val();
    mood_category_id = $("input:radio[name ='mood_category_id']:checked").val();
    product_category_id = $("input:radio[name ='product_category_id']:checked").val();
    console.log(product_id);
    if ($(element).text() == 'Remove')
    {
        $.ajax({
        url: 'removeRecommendProduct.php',
        type: 'POST',
        data: {'weekday': weekday, 'mood_category_id': mood_category_id, 'product_id': product_id} 
        }).done(function (data) {
            if (data.result)
            {
                $(element).parent().parent().toggleClass('inlist');
                $(element).text('Add');
            }
            else
            {
                alert ('Update fail!');
            }
        });    }
    else
    {
        $.ajax({
        url: 'addRecommendProduct.php',
        type: 'POST',
        data: {'weekday': weekday, 'mood_category_id': mood_category_id, 'product_id': product_id} 
        }).done(function (data) {
            //because inserting a product to recommend product list does not generating a new id
            //the return value could be 0 if success.
            if (data.result === false)
            {
                alert ('Update fail!');
            }
            else
            {
                $(element).parent().parent().toggleClass('inlist');
                $(element).text('Remove');
            }
        });
    }

}

$('.addProduct').click(function () {
    $('#addProductContainer').foundation('reveal', 'open');  
});

$('.getLCBOProduct').click(function () {
    if ($('#lcbo_product_id_a').valid())
    {
        $.ajax({
            url: 'http://lcboapi.com/products/' + $('#lcbo_product_id_a').val(),
            type: 'GET',
            dataType: 'jsonp'
        }).done(function (data) {
            console.log(data);
            if (data.result) {
                $('#product_name_a').val(data.result.name);
                $('#product_volume_a').val(data.result.volume_in_milliliters);
                $('#product_price_a').val(data.result.price_in_cents);
                $('#product_imageURL_a').val(data.result.image_url);                
            }
            else
            {
                alert (data.message);
            }
        });
    }
});

$('.updateLCBOProduct').click(function () {
    if ($('#lcbo_product_id_e').valid())
    {
        $.ajax({
            url: 'http://lcboapi.com/products/' + $('#lcbo_product_id_e').val(),
            type: 'GET',
            dataType: 'jsonp'
        }).done(function (data) {
            console.log(data);
            if (data.result) {
                $('#product_name_e').val(data.result.name);
                $('#product_volume_e').val(data.result.volume_in_milliliters);
                $('#product_price_e').val(data.result.price_in_cents);
                $('#product_imageURL_e').val(data.result.image_url);                
            }
            else
            {
                alert (data.message);
            }
        });
    }
});


function addProduct() {
//    e.preventDefault();
    name = $('#product_name_a').val();
    volume = $('#product_volume_a').val();
    price = $('#product_price_a').val();
    imageURL = $('#product_imageURL_a').val();
    lcbo_product_id = $('#lcbo_product_id_a').val();
    product_category_id = $("input:radio[name ='product_category_id']:checked").val();
    weekday = $("input:radio[name ='weekday']:checked").val();
    mood_category_id = $("input:radio[name ='mood_category_id']:checked").val();    
    console.log("add");

    $.ajax({
        url: 'addProduct.php',
        type: 'POST',
        data: {'name': name, 'volume': volume, 'price': price, 'imageURL': imageURL, 'lcbo_product_id': lcbo_product_id, 'product_category_id': product_category_id} 
    }).done(function (data) {
        if (data.result === -1)
        {
            alert ('LCBO product ID already exists!');
        }
        else if (data.result === false)
        {
            alert ('Fail to add product!');
        }
        else
        {
            product_id = data.result;
            position = 0;

            $('.listProductName').each(function () {
               if ($(this).text().toUpperCase() < name.toUpperCase()) 
                   position++;
            });   
            
            console.log(position);

            table = document.getElementById("productlist");

            row = table.insertRow(position+1);
            cell1 = row.insertCell(0);
            cell2 = row.insertCell(1);
            cell3 = row.insertCell(2);
            cell4 = row.insertCell(3);
            cell5 = row.insertCell(4);
            cell1.innerHTML = '<a class="listProductName">'+ name + '</a>';
            cell2.innerHTML = volume + ' ml';
            cell3.innerHTML = formatPrice(price);
            cell4.innerHTML = lcbo_product_id;
            cell5.innerHTML = '<input type="hidden" value="' + product_id + '">' + showAction(false);
            $.ajax({
                url: 'addRecommendProduct.php',
                type: 'POST',
                data: {'weekday': weekday, 'mood_category_id': mood_category_id, 'product_id': product_id} 
            }).done(function (data) {
                //because inserting a product to recommend product list does not generating a new id
                //the return value could be 0 if success.
                if (data.result === false)
                {
                    alert ('Update fail!');
                }
                else
                {
                    row.className = 'inlist';
                    cell5.innerHTML = '<input type="hidden" value="' + product_id + '">' + showAction(true);
                }
                bindAction();
                $('#product_name_a').val('');
                $('#product_volume_a').val('');
                $('#product_price_a').val('');
                $('#product_imageURL_a').val('');
                $('#lcbo_product_id_a').val('');                
                $('#addProductContainer').foundation('reveal', 'close');
            });            

        }
    });
  
};


function updateProduct()
{
    name = $('#product_name_e').val();
    volume = $('#product_volume_e').val();
    price = $('#product_price_e').val();
    imageURL = $('#product_imageURL_e').val(); 
    product_id = $('#product_id_e').val();
    inlist = $('#product_inlist_e').val();
    lcbo_product_id = $('#lcbo_product_id_e').val();
    $.ajax({
        url: 'updateProduct.php',
        type: 'POST',
        data: {'name': name, 'volume': volume, 'price': price, 'imageURL': imageURL, 'product_id': product_id} 
    }).done(function (data) {
        if (data.result === false)
        {
            alert ('Fail to update product!');
        }
        else
        {
            console.log('update success');
            //remove the old row and create a new row
            $('.ToggleProduct').each(function () {
               if ($(this).parent().find('input[type=hidden]').val() == product_id) 
               {
                   $(this).parent().parent().remove();
               }
            });
            
            position = 0;

            $('.listProductName').each(function () {
               if ($(this).text().toUpperCase() < name.toUpperCase()) 
                   position++;
            });   
            
            console.log(position);

            table = document.getElementById("productlist");

            row = table.insertRow(position+1);
            cell1 = row.insertCell(0);
            cell2 = row.insertCell(1);
            cell3 = row.insertCell(2);
            cell4 = row.insertCell(3);
            cell5 = row.insertCell(4);
            if (inlist == 'true')
            {
                row.className = 'inlist';
            }
            cell1.innerHTML = '<a class="listProductName">'+ name + '</a>';
            cell2.innerHTML = volume + ' ml';
            cell3.innerHTML = formatPrice(price);
            cell4.innerHTML = lcbo_product_id;
            cell5.innerHTML = '<input type="hidden" value="' + product_id + '">' + showAction(inlist == 'true' ? true : false);
            bindAction();
            $('#editProductContainer').foundation('reveal', 'close'); 
        }
    });
}

$('.delteProduct').click(function () {
   if (confirm('This product will be removed from all recommend lists, are you sure?'))
   {
       product_id = $('#product_id_e').val();
       $.ajax({
           url: 'deleteProduct.php',
           type: 'POST',
           data: {
               'product_id': product_id
           }
       }).done(function (data) {
            if (data.result === false)
            {
                alert ('Fail to delete product!');
            }
            else
            {
                console.log('delete success');
                $('.ToggleProduct').each(function () {
                   if ($(this).parent().find('input[type=hidden]').val() == product_id) 
                   {
                       $(this).parent().parent().remove();
                   }
                });
                $('#editProductContainer').foundation('reveal', 'close'); 
            }           
       });
   }
});

$("#addProductForm").validate({
    errorClass: "error",
    rules: {
        lcbo_product_id: {
            required: true,
            digits: true
        },
        name: {
            required: true
        },
        volume: {
            required: true,
            digits: true
        },
        price: {
            required: true,
            digits: true
        }
    },
    messages: {
        lcbo_product_id: {
            required: "Please enter LCBO product id.",
            digits: "Please enter valid number."
        },
        name: {
            required: "Please enter product name."
        },
        volume: {
            required: "Please enter product volume.",
            digits: "Please enter valid number."
        },
        price: {
            required: "Please enter product price.",
            digits: "Please enter valid number."
        }
    },
    submitHandler: function() {
        console.log("No validation errors!");
//        $("#addProductForm").submit();
        addProduct();
    }
});

$("#editProductForm").validate({
    errorClass: "error",
    rules: {
        lcbo_product_id: {
            required: true,
            digits: true
        },
        name: {
            required: true
        },
        volume: {
            required: true,
            digits: true
        },
        price: {
            required: true,
            digits: true
        }
    },
    messages: {
        lcbo_product_id: {
            required: "Please enter LCBO product id.",
            digits: "Please enter valid number."
        },
        name: {
            required: "Please enter product name."
        },
        volume: {
            required: "Please enter product volume.",
            digits: "Please enter valid number."
        },
        price: {
            required: "Please enter product price.",
            digits: "Please enter valid number."
        }
    },
    submitHandler: function() {
        console.log("No validation errors!");
//        $("#addProductForm").submit();
        updateProduct();
    }
});