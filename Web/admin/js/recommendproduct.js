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
            $('#productlist').append('<tr class="' + (isInList(data.result[i].recommend_category_id) ? "inlist" : "") +'"><td>' + data.result[i].name + '</td><td>' + data.result[i].volume + '  ml</td><td>' + formatPrice(data.result[i].price) + '</td><td>' + (isInList(data.result[i].recommend_category_id) ? "Remove" : "Add") +'</td></tr>');
        }
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