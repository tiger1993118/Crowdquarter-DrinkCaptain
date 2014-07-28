//set the animation while running AJAX code
$(document).on({
    ajaxStart: function() { $("#loading_progress").show(); },
    ajaxStop: function() { $("#loading_progress").hide(); }    
});

//when click the weekday, show the mood and hide the categories and products
$('.weekday').click(function () {
    $('#moodCategories').removeClass('hide');
    $('#productCategories').addClass('hide');
    $('#products').addClass('hide');
    //clear the checked option of mood
    $('.moodCategoryID').each(function () {
        $(this).prop('checked', false);
    });
    //clear the checked option of category
    $('.productCategoryID').each(function () {
        $(this).prop('checked', false);
    });
});

//when click the mood, show the categories and hide the products
$('.moodCategoryID').click(function () {
    $('#productCategories').removeClass('hide');
    $('#products').addClass('hide');
    //clear the checked option of category
    $('.productCategoryID').each(function () {
        $(this).prop('checked', false);
    });
});

//when click the category, show the products
$('.productCategoryID').click(function () {
    $('#products').removeClass('hide');
    //set the table head of product list
    $('#productlist').html('<tr><th>Name</th><th>Volume</th><th>Price</th><th>LCBO ID</th><th></th></tr>');

    showProducts();
});

//show products
function showProducts()
{
    //get the checked weekday, mood and category
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
            //create each product in the table
            //if the recommend_category_id is not null, means the product is in current recommend combination, add the class "inlist" to the TR
            //if in the recommend list, show action as "Remove", otherwise show the action as "Add"
            $('#productlist').append('<tr class="' + (isInList(data.result[i].recommend_category_id) ? "inlist" : "") +'"><td><a class="listProductName">' + data.result[i].name + '</a></td><td>' + data.result[i].volume + '  ml</td><td>' + formatPrice(data.result[i].price) + '</td><td>' + data.result[i].lcbo_product_id +  '</td><td><input type="hidden" value="' + data.result[i].product_id + '">' + showAction(data.result[i].recommend_category_id) +'</td></tr>');
        }
        //after the products shown, bind the actions to the link of product name and action
        bindAction();
    });
}

//format the displayed price since it is in cents from database
function formatPrice(input)
{
    return '$' + (input/100);
}

//check if the product is in the recommend product list
//the input is the recommend_category_id of the product list, null means not in list
function isInList(input)
{
    if (!input)
        return false;
    else
        return true;
}

//show action as "Add" or "Remove" based on if the product is in recommend list
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

//bind the action to the link of product name and action
function bindAction()
{
    //when click the action, add or remove the product to/from the list
    $('.ToggleProduct').unbind('click').click(function () {
        toggleProduct($(this));
    });
    //when click the product name, show the edit product form
    $('.listProductName').unbind('click').click(function () {
        showEditForm($(this));
    });
}

//show the edit product form
//the paramenter "element" refers to the <a> tag of product name clicked
function showEditForm(element)
{
    //get the clicked product_id
    product_id = $(element).parent().parent().find('input[type=hidden]').val();
    //check if it is in the recommend list
    //this will be used later when a product is updated and recreate the record in the product list table
    inlist = $(element).parent().parent().hasClass('inlist');
    
    //get the product information and show in the form
    $.ajax({
        url: 'getProduct.php',
        type: 'POST',
        data: {'product_id': product_id}
    }).done(function (data) {
        if (data.Product)
        {
            //show the edit form
            $('#editProductContainer').foundation('reveal', 'open');
            //set the value of each field
            $('#product_id_e').val(data.Product[0].id);
            $('#lcbo_product_id_e').val(data.Product[0].lcbo_product_id);
            $('#product_name_e').val(data.Product[0].name);
            $('#product_volume_e').val(data.Product[0].volume);
            $('#product_price_e').val(data.Product[0].price);
            $('#product_imageURL_e').val(data.Product[0].imageURL);
            $('#product_inlist_e').val(inlist);
        }
    });
//    console.log(product_id);
}

//add or remove the product to/from the recommend product list
//the paramenter "element" refers to the <a> tag of the action clicked
function toggleProduct(element)
{
    //get the clicked product_id
    product_id = $(element).parent().find('input[type=hidden]').val();
    //get the weekday, mood, product category
    weekday = $("input:radio[name ='weekday']:checked").val();
    mood_category_id = $("input:radio[name ='mood_category_id']:checked").val();
    product_category_id = $("input:radio[name ='product_category_id']:checked").val();
//    console.log(product_id);
    //if the action is "Remove", remove the product from recommend list
    if ($(element).text() == 'Remove')
    {
        $.ajax({
        url: 'removeRecommendProduct.php',
        type: 'POST',
        data: {'weekday': weekday, 'mood_category_id': mood_category_id, 'product_id': product_id} 
        }).done(function (data) {
            if (data.result)
            {
                //if success
                //toggle the class "inlist" of the "TR" tag
                $(element).parent().parent().toggleClass('inlist');
                //change the action to "Add"
                $(element).text('Add');
            }
            else
            {
                alert ('Update fail!');
            }
        });    
    }
    //if the action is "Add" (not "Remove"), add the product to recommend list
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
                //if success
                //toggle the class "inlist" of the "TR" tag
                $(element).parent().parent().toggleClass('inlist');
                //change the action to "Remove"
                $(element).text('Remove');
            }
        });
    }

}

//when click the "Add new product" link, show the add product form
$('.addProduct').click(function () {
    $('#addProductContainer').foundation('reveal', 'open');  
});

//get product information from LCBO API while clicking the "Get" button
$('.getLCBOProduct').click(function () {
    if ($('#lcbo_product_id_a').valid())
    {
        $.ajax({
            url: 'http://lcboapi.com/products/' + $('#lcbo_product_id_a').val(),
            type: 'GET',
            dataType: 'jsonp'
        }).done(function (data) {
            console.log(data);
            //if data.result is not null, means return the product data successfully
            if (data.result) {
                $('#product_name_a').val(data.result.name);
                $('#product_volume_a').val(data.result.volume_in_milliliters);
                $('#product_price_a').val(data.result.price_in_cents);
                $('#product_imageURL_a').val(data.result.image_url);                
            }
            //if data.result is null, show the returned message
            else
            {
                alert (data.message);
            }
        });
    }
});

//update the product information from LCBO API while editing a product.
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

//add a new product
function addProduct() {
//    e.preventDefault();
    //get all needed data
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
        //check if the LCBO product ID already exists in database
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
            //get the returned product_id if adding product successfully
            product_id = data.result;
            
            //check which position the product should be shown in the product list table
            //the table is sorted by product name
            position = 0;

            $('.listProductName').each(function () {
               if ($(this).text().toUpperCase() < name.toUpperCase()) 
                   position++;
            });   
            
            console.log(position);

            table = document.getElementById("productlist");
            //create the new row in the table and insert it into the correct position
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
            
            //then add the product to the recommend list
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
                    //if success, update the "TR" tag to add the "inlist" class
                    row.className = 'inlist';
                    //update the action cell, set it to "Remove"
                    cell5.innerHTML = '<input type="hidden" value="' + product_id + '">' + showAction(true);
                }
                
                //bind the click action to product name and action
                //need to bind it again since new product is created in the table
                bindAction();
                
                //clear the input boxes
                $('#product_name_a').val('');
                $('#product_volume_a').val('');
                $('#product_price_a').val('');
                $('#product_imageURL_a').val('');
                $('#lcbo_product_id_a').val('');   
                
                //close the add product form
                $('#addProductContainer').foundation('reveal', 'close');
            });            

        }
    });
  
};

//update a product
function updateProduct()
{
    //get all needed data
    name = $('#product_name_e').val();
    volume = $('#product_volume_e').val();
    price = $('#product_price_e').val();
    imageURL = $('#product_imageURL_e').val(); 
    product_id = $('#product_id_e').val();
    inlist = $('#product_inlist_e').val();
    lcbo_product_id = $('#lcbo_product_id_e').val();
    
    //update the product
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
            //the reason is the product name may be updated, need to put the row in the correct position
            $('.ToggleProduct').each(function () {
               if ($(this).parent().find('input[type=hidden]').val() == product_id) 
               {
                   //remove the old row if the product_id match
                   $(this).parent().parent().remove();
               }
            });
            
            //get the new position or the new row, same as the process of adding a new product
            position = 0;

            $('.listProductName').each(function () {
               if ($(this).text().toUpperCase() < name.toUpperCase()) 
                   position++;
            });   
            
            console.log(position);

            //create the new row for the updated product
            table = document.getElementById("productlist");

            row = table.insertRow(position+1);
            cell1 = row.insertCell(0);
            cell2 = row.insertCell(1);
            cell3 = row.insertCell(2);
            cell4 = row.insertCell(3);
            cell5 = row.insertCell(4);
            //check if before the update, it is in the recommend list, the "inlist" class also applies to the "TR" tag after edit
            if (inlist == 'true')
            {
                row.className = 'inlist';
            }
            cell1.innerHTML = '<a class="listProductName">'+ name + '</a>';
            cell2.innerHTML = volume + ' ml';
            cell3.innerHTML = formatPrice(price);
            cell4.innerHTML = lcbo_product_id;
            //if in the recommend list, action is "Remove", otherwise the action is "Add"
            cell5.innerHTML = '<input type="hidden" value="' + product_id + '">' + showAction(inlist == 'true' ? true : false);
            //bind the action of product name and action link again, since a new row is created.
            bindAction();
            //close the edit product form
            $('#editProductContainer').foundation('reveal', 'close'); 
        }
    });
}

//delete a product
$('.delteProduct').click(function () {
    //confirm before delete
   if (confirm('This product will be removed from all recommend lists, are you sure?'))
   {
       //get the product id
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
                //if success, remove the product from the product list table
                $('.ToggleProduct').each(function () {
                   if ($(this).parent().find('input[type=hidden]').val() == product_id) 
                   {
                       $(this).parent().parent().remove();
                   }
                });
                
                //close the edit product form 
                $('#editProductContainer').foundation('reveal', 'close'); 
            }           
       });
   }
});

//use the jquery validation plugin to validate the add product form
$("#addProductForm").validate({
    //the error message class ("error" is a built-in class of foundation)
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
        //when submit the form, if no validation error, call the addProduct function
        addProduct();
    }
});

//use the jquery validation plugin to validate the edit product form, same as above
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