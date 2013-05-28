<head>
  <meta http-equiv="content-type" content="text/html" />
  <meta name="author" content="victo" />
  <link href="css/login.css" rel="stylesheet" type="text/css" />
  <title>Test Login</title>
  <script src="js/jquery.min.js"></script>
  <script src="js/jquery.validate.js" type="text/javascript"></script>
  <script type="text/javascript">
    (function(a){a.fn.vAlign=function(){return this.each(function(){var b=a(this).height(),c=a(this).outerHeight(),b=(b+(c-b))/2;a(this).css("margin-top","-"+b+"px");a(this).css("top","50%");a(this).css("position","absolute")})}})(jQuery);(function(a){a.fn.hAlign=function(){return this.each(function(){var b=a(this).width(),c=a(this).outerWidth(),b=(b+(c-b))/2;a(this).css("margin-left","-"+b+"px");a(this).css("left","50%");a(this).css("position","absolute")})}})(jQuery);
    $(document).ready(function() {
      if($('#login-wrapper').length) {
        $("#login-wrapper").vAlign().hAlign()
      };
      if($('#login-validate').length) {
        $('#login-validate').validate({
          onkeyup: false,
          errorClass: 'error',
          rules: {
            login_name: { required: true },
            login_password: { required: true }
			login_password2: { required: true }
          }
        })
      }
      if($('#forgot-validate').length) {
        $('#forgot-validate').validate({
          onkeyup: false,
          errorClass: 'error',
          rules: {
            forgot_email: { required: true, email: true }
          }
        })
      }
      $('#pass_login').click(function() {
        $('.panel:visible').slideUp('200',function() {
          $('.panel').not($(this)).slideDown('200');
        });
        $(this).children('span').toggle();
      });
    });
	

	</script>
	
	<script type="text/javascript">
	function validateForm()
	{
		//alert("123");
		var username=document.getElementById('username');
		var pass1=document.getElementById('password1');
		var pass2=document.getElementById('password2');
		if (username.value == ""){
			alert("Please enter username");
			return false;	
		}
		if (pass1.value == ""){
			alert("Please enter password");
			return false;	
		}
		if (pass2.value == ""){
			alert("Please enter retype password");
			return false;	
		}	
		
		if (!pass2.value.equals(pass1.value)){
			alert("Please enter password the same in both field");
			return false;	
		}	
		return true;
	}
  </script>
</head>

<body>
<div class="container">
  <div class="login clearfix" id="login-wrapper" style="margin-top: 8%;">
    <div class="main-col">
      <img class="logo_img" alt="" src="images/logo.png">
      <div class="panel">
        <p class="heading_main">Register new account</p>
        <form id="login-validate" action="/register" method="post" onsubmit="return validateForm()" >
          <label for="username">Login</label>
          <input type="text" id="username" name="username" />
          <label for="password1">Password</label>
          <input type="password" id="password1" name="password1" />
		  <label for="password2">Retype Password</label>
		  <input type="password" id="password2" name="password2" />
		  <label for="blockquote">Term of Agreement</label>
		  <blockquote style="background-color: rgb(240, 231, 231)">
			<p style="font-family: monospace;
font-size: 12;">the downloading, copying and use of the Content will not infringe the proprietary rights, including but not limited to the copyright, patent, trademark or trade secret rights, of any third party</p>
		  </blockquote>
          <!--label for="login_remember" class="checkbox" style="margin-top: 20px;"><input type="checkbox" id="login_remember" name="login_remember" /> I agree 
		  </label-->		  
		  
          <div class="submit_sect">
            <button type="submit" class="btn btn-beoro-3">Submit</button>
          </div>
        </form>
      </div>
      <!--
      <div class="panel" style="display:none">
        <p class="heading_main">Can't sign in?</p>
        <form id="forgot-validate" method="post">
          <label for="forgot_email">Your email adress</label>
          <input type="text" id="forgot_email" name="forgot_email" />
          <div class="submit_sect">
            <button type="submit" class="btn btn-beoro-3">Request New Password</button>
          </div>
        </form>
      </div>
      -->
    </div>
  </div>
</div>

</body>
</html>
