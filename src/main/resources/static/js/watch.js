


function onLoadFunction(){
    oldvalue = "0";
    sum = "0,5;";
    // var address = document.getElementById("myvideo_html5_api").src;
    // post_data = {"requestedAddress":address}
    // $.ajax({
    //     url: "/getratings",
    //     type: "GET",
    //     data: post_data,
    //     success: function (data) {
    //         console.log(data);
    //
    //        constructEveryRating(data);
    //
    //     },
    // });

    var slider = document.getElementById("myRange");
    var output = document.getElementById("demo");
    output.innerHTML = slider.value;
    // slider.mouseup = function() {
    //     output.innerHTML = this.value;
    //     sum = Math.round(afterglow.getPlayer('myvideo').currentTime()) + ","+slider.value+";";
    //     console.log(sum);
    // }
    $("#myRange").mouseup(function(){
            output.innerHTML = this.value;
            sum = sum + Math.floor(afterglow.getPlayer('myvideo').currentTime()) + ","+slider.value+";";

    });





}



function saveRating(needOld) {

    var ret = ProcessingString(sum);
    var data = ret["data"];
    var label = ret["label"];
    var n = data.length-1;
    var temp = {'x':Math.floor(afterglow.getPlayer('myvideo').duration()),'y':data[n]['y']};
    data.push(temp);
    label.push(data[n]['y']);
    var needupdate = document.getElementById("charParent");
    needupdate.innerHTML = '<canvas id="myChart" style="display: block; height: 320px; width: 618px;" width="1236" height="300" class="chartjs-render-monitor"></canvas>';

    var ctx = document.getElementById('myChart');
    if(!needOld){
        ShowChart(ctx,data,label);
    }

    var address = document.getElementById("video").src;
    console.log(address);

    sum = sum + Math.floor(afterglow.getPlayer('myvideo').duration())+','+data[n]['y']+';';
    if(needOld){
        var attribute = oldvalue;
    }else{
        var attribute = document.getElementById("slct").value;
    }

    console.log(attribute)

    post_data = {"evaluation":sum, "videoAddress":address,"attribute":attribute};
    $.ajax({
        url: "/saveRating",
        type: "POST",
        data: post_data,
        success: function (data) {

            console.log(data);

        },
    });
    var slider = document.getElementById("myRange");
    sum = "0," + slider.value +";";
    console.log(sum);
}


function ProcessingString(sum)
{

    var n=sum.split(";");
    var x=[]
    var y=[]

    for (var i=0;i<n.length-1;i++)
    {

        var basic = n[i].split(",");
        var a = basic[0];
        var b = basic[1];
        x.push(a);
        y.push(b);
    }



    data = [];
    for(var i =0 ;i < x.length; i++){
        var point = {"x":x[i],"y":y[i]};
        data.push(point);
    }
    var ret = {"data":data,"label":x};
    return ret;
}

function ShowChart(ctx, data, label) {
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: label,
            datasets: [{
                label: "Score",
                lineTension: 0.3,
                backgroundColor: "rgba(78, 115, 223, 0.05)",
                borderColor: "rgba(78, 115, 223, 1)",
                pointRadius: 3,
                pointBackgroundColor: "rgba(78, 115, 223, 1)",
                pointBorderColor: "rgba(78, 115, 223, 1)",
                pointHoverRadius: 3,
                pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                pointHitRadius: 10,
                pointBorderWidth: 2,
                data: data,
            }],
        },
        options: {
            maintainAspectRatio: false,
            layout: {
                padding: {
                    left: 10,
                    right: 25,
                    top: 25,
                    bottom: 0
                }
            },
            scales: {
                xAxes: [{
                    type: "linear",
                    time: {
                        unit: 'time'
                    },
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        maxTicksLimit: 5,
                        padding: 10,
                        // Include a dollar sign in the ticks
                        callback: function (value, index, values) {
                            return +number_format(value);
                        }
                    },
                    gridLines: {
                        color: "rgb(234, 236, 244)",
                        zeroLineColor: "rgb(234, 236, 244)",
                        drawBorder: false,
                        borderDash: [2],
                        zeroLineBorderDash: [2]
                    }
                }],
            },
            legend: {
                display: false
            },
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                titleMarginBottom: 10,
                titleFontColor: '#6e707e',
                titleFontSize: 14,
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                intersect: false,
                mode: 'index',
                caretPadding: 10,
                callbacks: {
                    label: function (tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
                    }
                }
            }
        }
    });

}


function constructEveryRating(data){
    var names = [];
    var sums = [];
    var ids = [];
    for( var i of data){
        names.push(i["ratedBy"]);
        sums.push(i["rating"]);
        ids.push("my"+i["id"]);
    }

    var areas = document.getElementById("otherRating");
    var elementsneeded = "";
    for( var j = 0; j < names.length; j ++){
        elementsneeded = elementsneeded + '<div class="card shadow mb-4">';
        elementsneeded = elementsneeded + '<a href="#collapseCardExample'+ids[j]+'" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="collapseCardExample'+ids[j]+'">';
        elementsneeded = elementsneeded + '<h6 class="m-0 font-weight-bold text-primary">'+ names[j]+'</h6></a>';
        elementsneeded = elementsneeded + '<div class="collapse" id="collapseCardExample'+ids[j]+'" style=""><div class="card-body">';
        elementsneeded = elementsneeded + '<div class="chart-area"><div class="chartjs-size-monitor"><div class="chartjs-size-monitor-expand"><div class=""></div></div><div class="chartjs-size-monitor-shrink"><div class=""></div></div></div>';
        elementsneeded = elementsneeded + '<canvas id="'+ ids[j]+'" style="display: block; height: 320px; width: 618px;" width="1236" height="300" class="chartjs-render-monitor"></canvas>';
        elementsneeded = elementsneeded + '</div></div></div></div>';




    }
    areas.innerHTML = elementsneeded;

    for(var k = 0; k < sums.length; k++){
        var singlesum = sums[k];

        var ret = ProcessingString(singlesum);
        var data = ret["data"];
        var label = ret["label"];
        var ctx = document.getElementById(ids[k]).getContext('2d');
        ShowChart(ctx,data,label);

    }




}

