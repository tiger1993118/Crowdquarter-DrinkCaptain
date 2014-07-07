<?php
require_once "../API/MoodCategory.php";
$objMoodCategory = new MoodCategory();
$allMoodCategories = $objMoodCategory->all();
?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="css/normalize.css" />
	<link rel="stylesheet" href="css/foundation.css" />      
</head>
<body>

    <div class="row">
        <table class="small-12">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
            </tr>
    <?php
    foreach ($allMoodCategories as $category)
    {
    ?>           
            <tr>
                <td><?php echo $category["name"]; ?></td>
                <td><?php echo $category["description"]; ?></td>
                <td>Edit</td>
                <td>Delete</td>
            </tr>
    <?php
    }
    ?>
        </table>
    </div>
   
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/foundation/foundation.js"></script>
    <script>
      $(document).foundation();
    </script>
</body>
</html>