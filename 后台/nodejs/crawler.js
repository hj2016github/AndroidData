var http = require('https')
var url = 'https://www.baidu.com/'

http.get(url,function(res){
	var html = ''

	res.on('data',function(data){
		html += data
	})

	res.on('end',function(){
		console.log(html)
	})
}).on('error',function(){
	console.log('error!!')
})