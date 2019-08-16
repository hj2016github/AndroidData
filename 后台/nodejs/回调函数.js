//we是回调函数;
function learn(something){
	console.log(something)
}

//后续传递:后面的参数封装在函数中,作为起始函数callback的参数:
function we(callback,something){
	something += 'is cool'
	callback(something)
}

we(learn,'nodejs') //nodejs is cool:具名函数;

we(function(something){
	console.log(something)
},'jade')  //jade is cool:匿名函数;