<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AutomaticEmail Job SenderService</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>

<body class="container mt-5">

	<div class="card shadow">
        <div class="card-body">
            <h2 class="card-title mb-4">Upload Excel File</h2>

            <form action="upload" method="post"
				enctype="multipart/form-data">
                <div class="mb-3">
                    <input type="file" name="file" class="form-control"
						required />
                </div>
                <button type="submit" class="btn btn-primary">Send Emails</button>
            </form>
        </div>
    </div>


</body>

</html>