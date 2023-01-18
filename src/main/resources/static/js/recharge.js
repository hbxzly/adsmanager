$(function() {
    var editIndex = undefined;
    function endEditing(){
        if (editIndex == undefined){return true}
        if ($('#rechargeTable').datagrid('validateRow', editIndex)){
            $('#rechargeTable').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
    function onClickRow(index){
        /*console.log("点击行"+index);
        console.log("测试"+editIndex);*/
        if (editIndex != index){
            if (endEditing()){
                $('#rechargeTable').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#rechargeTable').datagrid('selectRow', editIndex);
            }
        }
    }

    $("#rechargeDialog").dialog({
        title: "账户充值",
        width: 850,
        height: 550,
        iconCls: "icon-edit",
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        draggable: true,
        closed: true,
        buttons: [
            {
                text:'提交',
                plain: true,
                iconCls:'icon-user_add',
                handler:function() {
                    if (endEditing()) {
                        var $dg = $('#rechargeTable');
                        if ($dg.datagrid('getChanges').length) {
                            var updated = $dg.datagrid('getChanges', "updated");//获取修改状态的行
                            if (updated.length) {
                                var adAccountRechargeVoList = JSON.stringify(updated);
                                $.ajax({
                                    type: "POST",
                                    url: "/selenium/adAccountRecharge",
                                    contentType: "application/json; charset=UTF-8",
                                    data: adAccountRechargeVoList,
                                    success: function (data) {

                                    }
                                });
                            }
                        }
                    }
                }
            },
        ],
        onBeforeOpen: function () {
        },
        onClose: function(){
        }
    });

    $("#rechargeTable").datagrid({
        title:'账户充值',
        iconCls:'icon-more',//图标
        border: true,
        collapsible:false,//是否可折叠的
        fit: true,//自动大小
        singleSelect:false,//是否单选
        pagination:true,//分页控件
        rownumbers:true,//行号
        pageSize: 20,
        onClickRow:onClickRow,
        columns: [
            [
                {field:'chk',checkbox: true,width:50},
                {field:'adAccountSystem',title:'飞书后台', sortable: true},
                {field:'id',title:'账户ID', sortable: true},
                {field:'balance',title:'余额'},
                {field:'spendAmount',title:'7天消耗'},
                {field:'payMethod',title:'支付方式',editor:{
                                                        type:'combobox',
                                                            options:{
                                                            valueField:'id',
                                                                textField:'name',
                                                                data:[{id:'0',name:'---'},
                                                                {id:'1',name:'支付宝'},
                                                                {id:'2',name:'微信'}]
                                                        }
                                                    },
                    formatter: function (value){
                        if (value == 0) {
                            return '';
                        }if (value == 1) {
                            return '支付宝';
                        }if (value == 2) {
                            return '微信';
                        }
                    }
                },
                {field:'rechargeAmount',title:'充值金额',editor: 'text'}
            ]
        ]
    });

    //设置分页控件
    var p = $('#rechargeTable').datagrid('getPager');
    $(p).pagination({
        // pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
    });

    //充值
    $("#recharge").click(function (){
        $("#rechargeDialog").dialog("open");
        var data = $("#dataList").datagrid("getSelections");
        var list = [];
        for (var i = 0; i <data.length; i++) {
            var arr = {};
            arr.adAccountSystem = data[i].adAccountSystem;
            arr.id = data[i].id;
            arr.balance = data[i].balance;
            arr.spendAmount = data[i].spendAmount;
            list.push(arr);
        }
        $("#rechargeTable").datagrid({
            data: list
        });
    });



});

