$(document).ready(function(){
    var msg = $("#msg").val();
    if(msg != "") layer.msg(msg);
    $("#addBtn").click(() => {
       location.href = "/user/addForm";
    });
    $("#importBtn").click(() => {
        layer.open({
            type: 1,
            title: "导入Excel",
            skin: 'layui-layer-rim', //加上边框
            area: ['420px', '240px'], //宽高
            content: `
               <form class="form-horizontal" id="form_table" action="/user/batchImport" enctype="multipart/form-data" method="post">
                    <input class="form-input" type="file" name="filename" />
                    <br/>
                    <button type="submit" class="btn">开始导入</button>
                </form>
            `
        });
    });
});

/** 根据开始时间和结束时间查询用户工作状态  **/
function search(){
    var flag = validateData();
    if(flag){
        $("#submitForm").attr("action", "/user/list").submit();
    }
}

/** Excel导出  **/
function exportExcel(){
    if( confirm('您确定要导出吗？') ){
        var fyXqCode = $("#fyXq").val();
        var fyXqName = $('#fyXq option:selected').text();
//	 		alert(fyXqCode);
        if(fyXqCode=="" || fyXqCode==null){
            $("#fyXqName").val("");
        }else{
//	 			alert(fyXqCode);
            $("#fyXqName").val(fyXqName);
        }
        $("#submitForm").attr("action", "/xngzf/archives/exportExcelFangyuan.action").submit();
    }
}

/** 批量删除 **/
function batchDel(){
    if($("input[name='IDCheck']:checked").size()<=0){
        layer.msg('至少选择一条!');
        return;
    }
    var allIDCheck = "";
    $("input[name='IDCheck']:checked").each(function(index, domEle){
        allIDCheck += $(domEle).val() + ",";
    });
    // 截掉最后一个","
    if(allIDCheck.length>0) {
        allIDCheck = allIDCheck.substring(0, allIDCheck.length-1);
        $("#ids").val(allIDCheck);
        if(confirm("您确定要批量删除这些记录吗？")){
            $("#submitForm").attr("action", "/user/delete").submit();
        }
    }
}

/** 普通跳转 **/
function jumpNormalPage(page){
    $("#submitForm").attr("action", "house_list.html?page=" + page).submit();
}

/** 输入页跳转 **/
function jumpInputPage(totalPage){
    // 如果“跳转页数”不为空
    if($("#jumpNumTxt").val() != ''){
        var pageNum = parseInt($("#jumpNumTxt").val());
        // 如果跳转页数在不合理范围内，则置为1
        if(pageNum<1 | pageNum>totalPage){
            layer.msg('请输入合适的页数，\n自动为您跳到首页');
            pageNum = 1;
        }
        $("#submitForm").attr("action", "house_list.html?page=" + pageNum).submit();
    }else{
        // “跳转页数”为空
        layer.msg('请输入合适的页数，\n自动为您跳到首页!');
        $("#submitForm").attr("action", "house_list.html?page=" + 1).submit();
    }
}