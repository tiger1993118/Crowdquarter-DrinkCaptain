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
    $('#productlist').html('<tr><th>Name</th><th>Volume</th><th>Price</th><th></th></tr>');

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
            $('#productlist').append('<tr class="' + (isInList(data.result[i].recommend_category_id) ? "inlist" : "") +'"><td>' + data.result[i].name + '</td><td>' + data.result[i].volume + '  ml</td><td>' + formatPrice(data.result[i].price) + '</td><td><input type="hidden" value="' + data.result[i].product_id + '">' + showAction(data.result[i].recommend_category_id) +'</td></tr>');
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
