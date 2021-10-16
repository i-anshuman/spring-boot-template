<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>Password Reset - Application</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Bree+Serif&display=swap" rel="stylesheet">
  <style type="text/css">
    :root {
      font-size: 16px;
    }
    body {
      font-family: "Bree Serif", sans-serif;
      color: indigo;
      background-color: #f1dfd1;
      background-image: linear-gradient(315deg, #f1dfd1 0%, #f6f0ea 74%);
    }
    .container {
      background-color: #d8dede;
      background-image: linear-gradient(315deg, #d8dede 0%, #e5bdf6 74%);
      margin: 20px 40px;
      padding: 60px 30px;
      border-radius: 10px;
      box-shadow: 0px 8px 20px 0px rgb(0 0 0 / 35%);
    }
    .welcome {
      margin: 0 0 10px 0;
      text-align: center;
    }
    .content {
      width: 70%;
      margin: 0 auto;
      text-align: justify;
    }
    .thanks > * {
      margin: 0;
    }
    a {
      text-decoration: none;
      color: blueviolet;
      word-wrap: break-word;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1 class="welcome">Hello ${username} !!</h1>
    <div class="content">
      <h3>You've requested to reset your password. In order to complete the process and access your account, you need to reset the password of your account.</h3>
      <h3>Please click on <a href="${link}">this link</a> or you can simply paste the link given below into a browser to reset the password and get access of your account.</h3>
      <p><a href="${link}">${link}</a></p>
      
      <h3>If you had not made this request, you can simply ignore this mail.</h3>
      
      <div class="thanks">
        <h4>Thank you :)</h4>
        <h5>- ${sender}</h5>
      </div>
    </div>
  </div>
</body>
</html>
