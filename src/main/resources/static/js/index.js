function o_excute() {
    postDoc("oracle",$('#o_ip').val(),$('#o_port').val(),$('#o_dbName').val(),$('#o_userName').val(),$('#o_password').val());
}
function m_excute() {
    postDoc("mysql",$('#m_ip').val(),$('#m_port').val(),$('#m_dbName').val(),$('#m_userName').val(),$('#m_password').val());
}
function s_excute() {
    postDoc("sqlserver",$('#s_ip').val(),$('#s_port').val(),$('#s_dbName').val(),$('#s_userName').val(),$('#s_password').val());
}
function postDoc(dbKind,ip,port,dbName,userName,password) {
    window.open("/dbExport/v1/makeWord?dbKind="+dbKind+"&ip="+ip+"&port="+port+"&dbName="+dbName+"&userName="+userName+"&password="+password);
}