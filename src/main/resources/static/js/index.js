function o_excute() {
    postDoc("oracle");
}
function m_excute() {
    postDoc("mysql")
}
function s_excute() {
    postDoc("sqlserver")
}
function postDoc(dbKind) {
    $('#messageText').text("正在努力生成中......");
    $('#myModal').modal('show');
    $.ajax({
        url: "/dbExport/makeWord",
        type: "post",
        data: {dbKind:dbKind,ip: $('#m_ip').val(),port:$('#m_port').val(),dbName:$('#m_dbName').val(), userName:$('#m_userName').val(), password:$('#m_password').val()},
        success: function (data) {
            console.log(data);
            $('#messageText').text(data.resultMsg);
        }, error: function (data) {
            console.log(data);
            $('#messageText').text("网络错误，请重试错误");
        }
    });
}