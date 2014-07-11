<?php

session_start();
if (!isset($_SESSION["role"]))
{
    header('Location: admin_login.php');
}
else
{
    if ($_SESSION["role"] != 'admin')
    {
        header('Location: admin_login.php');
    }
}
?>