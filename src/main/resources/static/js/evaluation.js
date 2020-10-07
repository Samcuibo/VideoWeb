function onLoadFunction(){


      // Get the evaluation criteria
    $.ajax({
        url: "/EvaluationCriteria",
        type: "GET",
        success: function (data) {

            // console.log(data);
            items = data.length;
            //construct evaluation table;
            constructEveryRow(data);
            idList = [];
            for(var single of data){
                idList.push('customRadio'+single['id']);
            }

        },
    });
    getAllOtherEvaluation();




}


function constructEveryRow(data){
    var needed = '';
    for(var single of data){
        needed = needed + '<tr role ="role" > <td name="customRadio'+single["id"] + '">' + single["oneCriterion"] + '</td><td><div>';
        needed = needed + '<div  style="margin-left: 5px; margin-right: 15px;"class="custom-control custom-radio "><input  type="radio" id="customRadio' + single['id'] +'1" name="customRadio' +single['id']  +'" class="custom-control-input"         > <label class="custom-control-label" for="customRadio'+ single['id']+'1">Excellent</label></div>';
        needed = needed + '<div  style="margin-left: 5px; margin-right: 15px;"class="custom-control custom-radio "><input  type="radio" id="customRadio' + single['id'] +'2" name="customRadio' + single['id'] +'" class="custom-control-input" checked > <label class="custom-control-label" for="customRadio'+ single['id']+'2">Good</label></div>';
        needed = needed + '<div  style="margin-left: 5px; margin-right: 15px;"class="custom-control custom-radio "><input  type="radio" id="customRadio' + single["id"] +'3" name="customRadio' + single['id'] +'" class="custom-control-input"         > <label class="custom-control-label" for="customRadio'+ single['id']+'3">Fair</label></div>';
        needed = needed + '<div  style="margin-left: 5px; margin-right: 15px;"class="custom-control custom-radio "><input  type="radio" id="customRadio' + single['id'] +'4" name="customRadio' + single['id'] +'" class="custom-control-input"         > <label class="custom-control-label" for="customRadio'+ single['id']+'4">Poor</label></div></div></td></tr>'

    }

    var needUpdated = document.getElementById("criteriaBody");
    needUpdated.innerHTML = needed;
}








function submitEvaluation(){
    var evaluation = [];
    for(var single of idList) {

        var getchoice = document.getElementsByName(single);
        evaluation.push(getchoice[0].innerText);
        if (getchoice[1].checked) {
            evaluation.push("Excellent");
        } else if (getchoice[2].checked) {
            evaluation.push("Good");
        } else if (getchoice[3].checked) {
            evaluation.push("Fair");
        } else if (getchoice[4].checked) {
            evaluation.push("Poor");
        }

    }


    post_data = {evaluation:evaluation,"address": document.getElementById("video").src};
    console.log(evaluation);


    $.ajax({
        url: "/saveEvaluation",
        type: "POST",
        dataType:'json',
        data: post_data,
        success: function (data) {

        //TO DO: reaction when save evaluation

        },
    });

    $("input[type=radio]").attr("disabled","true");

    $("#saveButton").attr("disabled","true");
    document.getElementById("saveButton").innerText="saved";
}



function getAllOtherEvaluation(){

    var address = document.getElementById("video").src;
    post_data = {"address":address};

    $.ajax({
        url: "/getOtherEvaluation",
        type: "GET",
        data:post_data,
        success: function (data) {
            console.log(data);
            constructAllOtherEvaluation(data);

        },
    });


}


function constructAllOtherEvaluation(data){
    var length = data.length;
    var need = "";
    for(var i =0; i< length; i++){
        if(data[i].length >0){
        need = need + '<div class="card shadow mb-4">';
        need = need + '<a href="#collapseCardExamplemy' + i + '" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="collapseCardExamplemy' + i + '">';
        need = need + '<h6 class="m-0 font-weight-bold text-primary">' + data[i][0]["criterion"] +'</h6>';
        need = need + ' </a> <div class="collapse" id="collapseCardExamplemy' + i + '" style="">';
        need = need + '<div class="card-body">';
        need = need + '<div class="dataTables_wrapper dt-bootstrap4">';
        need = need + '<div class="row"> <div class="col-sm-12"> ';
        need = need + '<table class="table table-bordered dataTable" id="dataTable' + i +'"width="100%" cellspacing="0" role="grid" aria-describedby="dataTable_info" style="width: 100%;">';
        need = need + '<thead> <tr role="row"><th class="sorting_asc" tabindex="0" aria-controls="dataTable' + i +'"' + 'rowspan="1" colspan="1" aria-sort="ascending" aria-label="Name: activate to sort column descending" style="width: 63px;">' + "Who made the evaluation" +  '</th><th class="sorting" tabindex="0" aria-controls="dataTable' + i +'"' + 'rowspan="1" colspan="1" aria-label="Position: activate to sort column ascending" style="width: 51px;">' + 'What they think</th>';
        need = need + ' </thead> <tbody>';
        for( var single of data[i]){
            need = need + '<tr role="row" >' +  '<td class="sorting_1">' + single['ratedBy'] + '</td>';
            need = need + '<td>'+ single['level'] + '</td>';
            need = need + '</tr>';
        }
        need = need + '</tbody></table></div></div></div></div></div></div>';
        }



    }
    var ele = document.getElementById("otherEvaluation");
    ele.innerHTML = need;



}