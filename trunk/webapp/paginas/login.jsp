<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br">
<head>
	<title>P&aacute;gina de acesso</title>
	<link href="css/estilos.css" rel="stylesheet" type="text/css" media="screen" />
</head>

<body class="loginBody">
<form action="j_security_check" method="post">
	<table align="center" >
		<tr>
			<td width="40%">
				<label>Login</label>
			</td>
			<td width="60%">
				<input type="text" name="j_username" id="j_username" size="15" maxlength="10" />
			</td>
		</tr>
		<tr>
			<td width="40%">
				<label>Senha</label>
			</td>
			<td width="60%">
				<input type="password" name="j_password" id="j_password" size="15" maxlength="10" />
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value=" Entrar " />
			</td>
		</tr>
	</table>
</form>
</body>
</html>
