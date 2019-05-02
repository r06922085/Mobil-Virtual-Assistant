<?php

$ques = $_GET['Str'];
$ans = iconv('BIG-5', 'UTF-8', exec("python answer.py $ques"));

echo $ans
?>