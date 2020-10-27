package data

class Question(
        var type:String,
        var title: String,
        var question:String,
        var useCase:Boolean,
        var answers:MutableList<AnswerVariant>,
        var id:Int
){
    override fun toString(): String = title
}
