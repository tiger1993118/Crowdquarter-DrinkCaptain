<?php
session_start();
$username = $_POST["username"];
$password = $_POST["password"];

if (($username == 'admin') and ($password == 'drinkC@pt'))
{
    $_SESSION["role"] = "admin";
    header("Location: admin_mood.php");
}
else
{
    header("Location: admin_login.php");
}

?>