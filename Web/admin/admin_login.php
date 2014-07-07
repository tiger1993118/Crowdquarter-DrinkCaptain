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
<div class='row loginForm'>
	<form action="admin_mood.php" method="post">
		<div class='columns large-offset-4 large-4 medium-offset-3 medium-6 small-offset-1 small-10'>
			<div class='row'>
				<div class='columns small-12'><label>Username</label></div>
				<div class='columns small-12'><input type=text name='username'></div>
			</div>
			<div class='row'>
				<div class='columns small-12'><label>Password</label></div>
				<div class='columns small-12'><input type=password name='password'></div>
			</div>
			<div class='row text-center'>
				<div class='columns small-12'><input type="submit" value="Submit" /></div>
			</div>
		</div>
	</form>
</div>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/foundation/foundation.js"></script>
    <script>
      $(document).foundation();
    </script>
</body>
</html>