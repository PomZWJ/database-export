$(function () {
    loadInfo();
});
function loadInfo() {
    $.ajax({
        url: ctx+"makeWord/getDocData",
        data: {
            ip :ip,
            port :port,
            dbName :dbName,
            userName :userName,
            password :password,
            dbKind: dbKind
        },
        success:function (data) {
            let resultCode = data.resultCode;
            if(resultCode == '000000'){
                f(data.params);
            }
        }
    });
}
function f(params) {
    let headerList = params.headerList;
    let tableDetailInfo = params.tableDetailInfo;
    let tableHtml = '';
    let headerHtml = '';
    for(let i=0;i<headerList.length;i++){
        headerHtml += '<th>'+headerList[i]+'</th>'
    }
    for(let i=0;i<tableDetailInfo.length;i++){
        let table = tableDetailInfo[i];
        let tabsColumn = table.tabsColumn;
        let tbodyHtml = "";
        for (let j = 0; j < tabsColumn.length; j++) {
            tbodyHtml += '<tr>';
            for (let k = 0; k < headerList.length; k++) {
                let column = tabsColumn[j];
                let columnElement = column[headerList[k]];
                if(columnElement == undefined || columnElement == '' ){
                    columnElement = "无";
                }
                tbodyHtml += '<td>' + columnElement + '</td>';
            }
            tbodyHtml += '</tr>';
        }



        tableHtml +=
            '<table class="table table-bordered">'+
            '<caption style="font-weight: bolder;font-size: 40px">'+table.tableName+'('+table.tableComments+')'+'</caption>'+
            '<thead>'+
            '<tr>'+headerHtml+'</tr>'+
            '</thead>'+
            '<tbody>'+tbodyHtml+'</tbody>'+
            '</table>';
    }
    $("#body").empty();
    $("#body").append(tableHtml);
}