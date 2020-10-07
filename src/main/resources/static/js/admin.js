$(".de").click(function(){

    var r = confirm("Do you really want to delete the item? (This operation can not be undone)");
    if (r == true) {
        var need = $(this).attr("del_uri");
        console.log(need);
        var post_data = {"criterion": need};


        $.ajax({
            url: "/deleteCriterion",
            type: "GET",
            data: post_data,
            success: function (data) {

                location.href="Deleted";

            },
        });




    } else {

    }
});

function ratingCriterion(){
    var criterion = document.getElementById("ratingCriterionAdd").value;
    console.log(criterion);
    post_data = {"criterion":criterion}

    $.ajax({
        url: "/addCriterion",
        type: "GET",
        data: post_data,
        success: function (data) {

            if(data == "ok")
            location.href="Added";

        },
    });

}

$(".dc").click(function(){

    var r = confirm("Do you really want to delete the item? (This operation can not be undone)");
    if (r == true) {
        var need = $(this).attr("del_uri");
        console.log(need);
        var post_data = {"criterion": need};


        $.ajax({
            url: "/deleteEvaluationCriterion",
            type: "GET",
            data: post_data,
            success: function (data) {

                location.href="Deleted";

            },
        });




    } else {

    }
});

function evaluationCriterion(){
    var criterion = document.getElementById("evaluationCriterionAdd").value;
    console.log(criterion);
    post_data = {"criterion":criterion}

    $.ajax({
        url: "/addEvaluation",
        type: "GET",
        data: post_data,
        success: function (data) {

            if(data == "ok")
                location.href="Added";

        },
    });

}