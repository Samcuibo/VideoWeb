function newSearch() {
    var post_data = {"userEntered": document.getElementById("aaa").value};
    console.log(post_data)

    $.ajax({
        url: "/search",
        type: "POST",
        data: post_data,
        success: function (data) {

            console.log(data);
            var items = data.length;
            constrcutResult(data);

        },
    });
}


function constrcutResult(data){
    var items = data.length;

    var insert = '<div class="col-xl-12 col-md-6 mb-4">\n' +
        '              <div class="card border-bottom-primary shadow h-100 py-2">\n' +
        '                <div class="card-body">\n' +
        '                  <div class="row no-gutters align-items-center">\n' +
        '                    <div class="col mr-2">\n' +
        '                      <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Search results</div>\n' +
        '                      <div id="searchResult" class="h5 mb-0 font-weight-bold text-gray-800">';

    insert = insert + ' Found '+ items + ' videos </div> </div> </div> </div> </div> </div>' ;

    for(var single of data){
        insert = insert +'          <div class="col-lg-12 mb-4">\n' +
            '            <!-- Project Card Example -->\n' +
            '            <div class="card shadow mb-4">\n' +
            '              <!-- Title name -->\n' +
            '              <div class="card-header py-3">\n' +
            '                <h6  class="m-0 font-weight-bold text-primary">' + single['title'] + ' </h6>\n' +
            '              </div>\n' +
            '              <div class="card-body">\n' +
            '                <div class="row no-gutters align-items-center">\n' +
            '                  <a href="/watch?requestedAddress=' + single['videoAddress'] + '" target="_blank">\n' +
            '                    <i class=\'fab fa-youtube\' style=\'font-size:48px;color:#4e73df\'></i>\n' +
            '                  </a>\n' +
            '                  &nbsp;\n' +
            '                  &nbsp;\n' +
            '                  <!--  uploaded by and abstract information-->' + '                  <div class="col mr-0">\n' +
            '                    <div class="m-0 font-weight-bold text-primary">' + single['uploadBy'] + '</div>\n' +
            '                    <div class="mb-0 small">' + single ['abstracts'] + '</div>\n' +
            '                  </div>\n' +
            '\n' +
            '              </div>\n' +
            '            </div>\n' +
            '          </div>\n' +
            '          </div>';
    }

    var element = document.getElementById( "rightplace");
    element.innerHTML = "";
    element.innerHTML = insert;

}

