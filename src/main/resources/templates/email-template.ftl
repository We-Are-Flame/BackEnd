<!DOCTYPE html>
<html >
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f6f6f6;
        }
        .container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .header {
            background-color: #ffffff;
            color: black;
            padding: 10px;
            padding-bottom: 0;
            text-align: center;
        }
        .content {
            padding: 10px;
            padding-top: 0;
            text-align: center;
        }
        .footer {
            background-color: #f2f2f2;
            color: #888888;
            text-align: center;
            padding: 10px;
            font-size: 12px;
        }
        .box {
            width: 30%;
            display: inline-block;
            padding: 10px 20px;
            margin: 20px 0;
            background-color: #646262;
            color: white;
            border-radius: 5px;
            font-size: 22px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Kitching</h1>
    </div>
    <div class="content">
        <h2>이메일 인증 번호</h2>
        <p class="box">${authCode}</p>
    </div>
    <div class="footer">
        <p>본 이메일은 발신 전용입니다. 궁금한 점이 있으시면 어플 문의하기를 통해 문의해주세요.</p>
        <p>&copy; Kitching </p>
    </div>
</div>
</body>
</html>