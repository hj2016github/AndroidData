var pet = {
	words: '...', //少了,号不行;
	speak: function(say){
		console.log(say + '' + this.words)
	}
}


var dog = {
	words : 'wang'
}

pet.speak.call(dog,'speak') //通过call改变pet上下文为dog-->this.word指向了"wang"
//而且使dog拥有了pet的speak方法;

