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
</head>
<%
	 String username = (String) request.getAttribute("username");  
	 if (username == null){
		username = "";
	 }
%>
<body>
<div class="container">
  <div class="login clearfix" id="login-wrapper" style="margin-top: 8%;">
    <div class="main-col">
      <img class="logo_img" alt="" src="images/logo.png">
      <div class="panel">
        <p class="heading_main">Account Login</p>
        <form id="login-validate" action="/login" method="post">
          <label for="login_name">Login</label>
          <input type="text" id="login-user" name="login-user" value="<%=username %>" />
          <label for="login_password">Password</label>
          <input type="password" id="login-password" name="login-password" />
          <label for="login_remember" class="checkbox"><input type="checkbox" id="login_remember" name="login_remember" /> Remember me 
		  </label>		  
		  <label><i><a href="/register.jsp" style="font-style: italic;">Want to be a new member?</a> </i></label>
          <div class="submit_sect">
            <button type="submit" class="btn btn-beoro-3">Login</button>
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
