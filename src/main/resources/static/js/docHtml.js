$(function () {
    loadInfo();
});
function loadInfo() {
    $.ajax({
        url: ctx+"v2/getTableData",
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
                loadTable(data.params);
            }else{
                alert(data.resultMsg);
            }
        }
    });
}
function loadTable(params) {
    let headerList = params.headerList;
    let fieldList = params.fieldList;
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
            for (let k = 0; k < fieldList.length; k++) {
                let column = tabsColumn[j];
                let columnElement = column[fieldList[k]];
                if(columnElement == undefined || columnElement == '' ){
                    columnElement = "无";
                }
                tbodyHtml += '<td>' + columnElement + '</td>';
            }
            tbodyHtml += '</tr>';
        }



        tableHtml +=
            '<table class="table table-bordered table-hover">'+
            '<caption style="font-weight: bolder;font-size: 30px">'+table.tableName+'('+table.tableComments+')'+'</caption>'+
            '<thead>'+
            '<tr>'+headerHtml+'</tr>'+
            '</thead>'+
            '<tbody>'+tbodyHtml+'</tbody>'+
            '</table>';
    }
    $("#body").empty();
    $("#body").append(tableHtml);
}