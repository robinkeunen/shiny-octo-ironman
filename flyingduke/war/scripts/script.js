/**
 * 
 */

<script>
   function time(pourcent) {
    var n = pourcent*100;
    $(".bar").css("width", n + "%");

function bar_up()
      {
      var xmlhttp;
      if (window.XMLHttpRequest)
      {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }
      else
      {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }

      xmlhttp.open("GET", "..." ,true);

      xmlhttp.onreadystatechange=function()
      {
      if (xmlhttp.readyState==4 && xmlhttp.status==200)
      {
        //document.getElementById("myDiv").innerHTML=;
        var res = JSON.parse(xmlhttp.responseText);
        time(res["result"]);
      }
      }

      xmlhttp.send();
      }

</script>