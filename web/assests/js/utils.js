$(document).ready(function () {

    $('.reserve-book').click(function () {
        reserveBook($(this));
    });
    $('.return-book').click(function () {
        returnBook($(this));
    });
});

function reserveBook(element) {
    var bookID = $(element).attr('id');
    $.ajax({
        type: 'get',
        url: contextUrl + '/frontController?command=reserveBook&bookID=' + bookID,
        success: function (response) {
            var str = "\"Auth required\"";

            if (response.toString() === str) {
                window.location.href = contextUrl + '/frontController?command=login';
            }
            else {
                $('#quantity').text(response);
                $('.reserve-book').attr('class', 'btn btn-secondary disabled').text(reserved);
            }
        }

    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    });
}

function returnBook(element) {
    var bookID = $(element).attr('id');
    $.ajax({
        type: 'get',
        url: contextUrl + '/frontController?command=returnBook&bookID=' + bookID,
        success: function (response) {
            $('.return-book#' + bookID).attr('class', 'btn btn-secondary disabled').text(returned);
        }

    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    });

}