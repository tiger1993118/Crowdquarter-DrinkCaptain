$(document).ready(function () {
    
});
function bindEditEvent()
{
    $('.editMoodCategory').unbind('click').click(function () {
        mood_category_id = $(this).parent().parent().find('.moodCategoryID').val();
        mood_category_name = $(this).parent().parent().find('.moodCategoryName').text();
        mood_category_description = $(this).parent().parent().find('.moodCategoryDescription').text();
        $('#mood_category_id').val(mood_category_id);
        $('#mood_category_name').val(mood_category_name);
        $('#mood_category_description').val(mood_category_description);
        $('#editCategryContainer').foundation('reveal', 'open');    
    });
}
function bindDeleteEvent()
{
    $('.deleteMoodCategory').unbind('click').click(function () {
        mood_category_id = $(this).parent().parent().find('.moodCategoryID').val();
        row = $(this).parent().parent();
        console.log(mood_category_id);
        if (confirm("Confirm delete?"))
        {
            $.ajax({
                url: 'deleteMood.php',
                type: 'POST',
                data: {'mood_category_id' : mood_category_id} 
            }).done(function (data) {
                if (!data.result)
                {
                    alert ('Delete fail, please try again!');
                }
                else
                {
                    $(row).remove();
                }
            });
        }
    });
}
bindEditEvent();
bindDeleteEvent();
$('#editCategoryForm').submit(function (e) {
    e.preventDefault();
    $.ajax({
        url: 'updateMood.php',
        type: 'POST',
        data: $(this).serialize() 
    }).done(function (data) {
        $('#editCategryContainer').foundation('reveal', 'close');
        if (!data.result)
        {
            alert ('Edit fail, please try again!');
        }
        else
        {
            mood_category_id = $('#mood_category_id').val();
            $('.moodCategoryID').each(function () {
                if ($(this).val() == mood_category_id)
                {
                    $(this).parent().parent().find('.moodCategoryName').text($('#mood_category_name').val());
                    $(this).parent().parent().find('.moodCategoryDescription').text($('#mood_category_description').val());
                }
            });
        }
    });
});

$('.addMoodCategory').click(function () {
    $('#addCategryContainer').foundation('reveal', 'open');  
});

$('#addCategoryForm').submit(function (e) {
    e.preventDefault(); 
    $.ajax({
        url: 'addMood.php',
        type: 'POST',
        data: $(this).serialize() 
    }).done(function (data) {
        $('#addCategryContainer').foundation('reveal', 'close');
        if (!data.result)
        {
            alert ('Add fail, please try again!');
        }
        else
        {
            mood_category_id = data.result;
            name=$('#mood_category_name_a').val();
            description=$('#mood_category_description_a').val();
            newCategory = '<tr><td><span class="moodCategoryName">' + name + '</span></td>' +
                '<td><span class="moodCategoryDescription">' + description + '</span></td>' +
                '<td><input type="hidden" class="moodCategoryID" value="' + mood_category_id + '"><a class="editMoodCategory">Edit</a></td>' +
                '<td><a class="deleteMoodCategory">Delete</a></td></tr>';
            $('#tblMoodCategories').append(newCategory);
            $('#mood_category_name_a').val('');
            $('#mood_category_description_a').val('');
            bindEditEvent();
            bindDeleteEvent();
        }
    });   
});



