$(function () {
    loadInfo();
    $("#db-contents").selectpicker({
        width: '300px',
    });
    window.onscroll = function () {
        scrollFunction();
    };
});

function scrollFunction() {
    if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
        document.getElementById("backToTopBtn").style.display = "block";
        $(".top-nav").css("box-shadow",'0px 2px 5px #888888');
    } else {
        document.getElementById("backToTopBtn").style.display = "none";
        $(".top-nav").css("box-shadow",'none');
    }
}

function selectOnChang(obj) {
    window.location.href = "#" + obj.value;
}

function topFunction() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    $("#db-contents").selectpicker('refresh');
}

function loadInfo() {
    $.ajax({
        url: ctx + "v2/getTableData",
        data: {
            ip: ip,
            port: port,
            dbName: dbName,
            userName: userName,
            password: password,
            dbKind: dbKind
        },
        success: function (data) {
            let resultCode = data.resultCode;
            if (resultCode == '000000') {
                loadTable(data.params);
            } else {
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
    for (let i = 0; i < headerList.length; i++) {
        headerHtml += '<th>' + headerList[i] + '</th>'
    }
    let dbContentsHmtl = new Array();
    for (let i = 0; i < tableDetailInfo.length; i++) {
        let table = tableDetailInfo[i];
        let tabsColumn = table.tabsColumn;
        let tbodyHtml = "";
        for (let j = 0; j < tabsColumn.length; j++) {
            tbodyHtml += '<tr>';
            for (let k = 0; k < fieldList.length; k++) {
                let column = tabsColumn[j];
                let columnElement = column[fieldList[k]];
                if (columnElement == undefined || columnElement == '') {
                    columnElement = "无";
                }
                tbodyHtml += '<td>' + columnElement + '</td>';
            }
            tbodyHtml += '</tr>';
        }
        let tableLable = table.tableName + '(' + table.tableComments + ')';
        tableHtml +=
            '<div id="' + tableLable + '" style="padding-top: 60px;">'+
            '<table class="table table-bordered table-hover">' +
            '<caption style="font-weight: bolder;font-size: 30px">' + tableLable + '</caption>' +
            '<thead>' +
            '<tr>' + headerHtml + '</tr>' +
            '</thead>' +
            '<tbody>' + tbodyHtml + '</tbody>' +
            '</table>'+
            '</div>';
        dbContentsHmtl.push('<option value="' + tableLable + '">' + tableLable + '</option>');
        $("#db-contents").empty();
        $("#db-contents").append(dbContentsHmtl.join(''));
        $("#db-contents").selectpicker('refresh');
    }
    $("#body").empty();
    $("#body").append(tableHtml);
}