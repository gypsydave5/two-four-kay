function exerciseHTTPMethodsForHAR() {
    var bodyMethods = ["POST", "PATCH", "PUT", "DELETE", "CONNECT", "OPTIONS"]
    var noBodyMethods = ["GET", "HEAD"]

    bodyMethods.map(function (method) {
        fetch(`/${method}`, {method: method, body: `testing ${method}`})
    })
    noBodyMethods.map(function (method) {
        fetch(`/${method}`, {method: method})
    })
}