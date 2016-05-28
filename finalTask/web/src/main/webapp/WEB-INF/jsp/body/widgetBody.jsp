<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Widget body</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link rel="stylesheet" type="text/css" href="/web/css/style.css"/>
		<script type="text/javascript">
			function deleteWidget(id) {
				document.deleteWidgetForm.Id.value = id;
				document.deleteWidgetForm.submit();
			}
		</script>
	</head>

	<body>
		<form method="POST" name="deleteWidgetForm" action="/web/edit?action=delete_widget">
			<input type="hidden" name="Id">
		</form>
	
		<table class="body">
			<tr class="title">
				<th width="5%">No.
				<th width="25%">Name
				<th width="20%">Metric type
				<th width="15%">Period
				<th width="15%">Refresh interval
				<th width="20%">Actions

			<c:forEach var="widget" items="${Widget}" varStatus="counter">
				<tr class="content">
					<td align="right">${counter.count}
					<td>${widget.name}
					<td>${widget.metricType.toString()}
					<td>${widget.period.toString()}
					<td>${widget.refreshInterval.toString()}
					<td>
						<input class="action" type="button" value="Modify" onClick="javascript:window.location='/web/widget/add'">
						<input class="action" type="button" value="Delete" onClick="javascript:deleteWidget('${widget.id}')">
			</c:forEach>
		</table>

		<a class="add" href="javascript:window.location='/web/widget/add'">Add widget</a>
	</body>
</html>