/*
 * 重写ajax，处理ajax session timeout 处理
 */
var responseText = "";
var errorDesc = "";
Ext.onReady(function(){
	if(errorDesc.indexOf("This session has been expired")>-1){
		Ext.MessageBox.show({title: 'Error',msg:'This session has been expired',width:120, height:100,
			fn: showResult,buttons: Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING});
	}
	Ext.Ajax.on('requestcomplete',function(conn, response, options, eOpts){  
		responseText = response.responseText;
		if(responseText.indexOf('<script type="text/javascript">')>-1){
			Ext.MessageBox.show({title:'Error',msg:'This session has been expired',width:120, height:100,
				fn: showTimeOutResult,buttons: Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING});
			
		}else if(responseText.indexOf("This session has been expired")>-1){
			Ext.MessageBox.show({title: 'Error',msg:'This session has been expired',width:120, height:100,
				fn: showResult,buttons: Ext.MessageBox.OK,icon:Ext.MessageBox.WARNING});
		}
    });
	function showTimeOutResult(){
		document.write(responseText); 
	}
	function showResult(btn){
		 window.top.location.href = "timeout.html";  
	}
	/***************************************/
	
	Ext.define('Ext.ux.ComboPageSize', {
	    requires: [
	        'Ext.form.field.ComboBox'
	    ],
	    pageSizes: [15, 25, 50, 100, 200],   
	    constructor : function(config) {
	        if (config) {
	            Ext.apply(this, config);
	        }
	    },
	    init : function(pbar){
	        var combo,
	            me = this;
	        combo = Ext.widget('combo', {
	            width: 70,
	            editable: false,
	            store: me.pageSizes,            
	            listeners: {
	                change: function (s, v) {                    
	                    pbar.store.pageSize = v;
	                    pbar.store.loadPage(1);
	                }
	            }
	        });
	        pbar.add(0, '-');
	        pbar.add(0, combo);
	        combo.setValue(pbar.store.pageSize);
	    }
	});
	
	Ext.define('Ext.ux.DynamicProgress',{
		config:{
			params:'',
			url:'',
			tipMsg:''
		},
		constructor : function(config) {
	        if (config) {
	        	config.task = {};
	        	config.msgBox = '';
	            Ext.apply(this, config);
	        }
	    },
	    stop:function(){
	    	this.msgBox.hide();
        	Ext.TaskManager.stop(this.task);
	    },
	    run:function(){
	    	var myUrl = this.url;
	    	var myParams = this.params;
	    	var myTipMsg = this.tipMsg;
	    	var myMsgBox = this.msgBox = Ext.MessageBox.show({
	            title: 'Processing status',
	            msg: myTipMsg,
	            progress: true,
	            wait:true,
	            width: 400,
	            buttons: Ext.Msg.OK,
	            scope: document.body,
	            fn:function(){
	            	Ext.TaskManager.stop(myTask);
	            }
	        });
	    	var myTask = this.task = {
	                run : function(){  
	                	Ext.Ajax.request({
	        	    		url: myUrl,
	                        params:myParams,
	                        success: function(response, opts){
	                        	var result = Ext.JSON.decode(response.responseText);
	                        	var dp = Ext.JSON.decode(result.DYNAMIC_PROGRESS);
	                        	var total = dp.total;
	                            var count = dp.count;
	                            if(total != 0){
	                            	if (count != total){
	                            		var percentage = count / total;
	                            		var percentageString=Number(percentage*100).toFixed(2);
	                            		percentageString+="%";
	                            		var processText = 'Total:' + total + '  Completed:' + count+'  Percentage:'+percentageString;
	                            		myMsgBox.updateProgress(percentage, processText, myTipMsg);
	                            	}else{
	                            		myMsgBox.hide();
	                            		Ext.TaskManager.stop(myTask);
	                            	}
	                            }else{
	                            	var percentage = 0;
                            		var percentageString='0.00';
                            		percentageString+="%";
                            		var processText = 'Total:' + total + '  Completed:' + count+'  Percentage:'+percentageString;
                            		myMsgBox.updateProgress(percentage, processText, myTipMsg);
	                            }
	                        },
	                        failure: function(response, opts){
	                        	myMsgBox.hide();
	                        	Ext.TaskManager.stop(myTask);
	                        }
	        	    	});
	                },  
	                interval : 1000
	            };
	    	Ext.TaskManager.start(myTask);
	    }
	});
});