<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Add dashboard</title>
		<link rel="stylesheet" type="text/css" href="/web/css/style.css"/>
	</head>

	<body>
		<form method="POST" name="dashboardForm" action="/web/edit">
			<input type=hidden name="action" value="add_dashboard">

			<table class="form">
				<tr>
					<td colspan="2" align="center">Dashboard settings</td>
				</tr>
				<tr>
					<td class="name">Name</td>
					<td>
						<input type="text" name="Name" size="30" maxlength="30">
					</td>
				</tr>
				<tr>
					<td class="name">Description</td>
					<td>
						<input type="text" name="Description" size="30" maxlength="100">
					</td>
				</tr>
				<tr>
					<td class="name">Widget</td>
					<td>
						<select name="Widget">
							<option disabled>Choose the widget</option>
							<c:forEach var="widget" items="${Widget}">
								<option value="${widget.id}">${widget.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</form>
		<form method="POST" name="cancelForm" action="/web/page?action=show_dashboard"></form>

		<input class="agree" type="submit" value="Save" onClick="JavaScript:document.dashboardForm.submit()">
		<input class="cancel" type="button" value="Back" onClick="JavaScript:document.cancelForm.submit()">
	</body>
</html>