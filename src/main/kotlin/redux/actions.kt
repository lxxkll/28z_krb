import data.Question
import data.QuestionGroup
import redux.RAction

//class zxc() : RAction

class ChangeQuestion(
    val newID:Int,
    val groupIndex:Int,
    val questionIndex:Int,
    val newTitle:String,
    val newQuestion:String,
    val newType:String,
    val newCase:Boolean ) : RAction // изменение вопроса и всех его свойств

class AddAnswerVariant(
    val groupIndex:Int,
    val questionIndex:Int,
    val newAnswer:String,
    val newFraction:Int,
    val newFeedback:String ) : RAction// добавление варианта ответа

class RemoveAnswerVariant(
    val groupIndex:Int,
    val questionIndex:Int,
    val answerIndex:Int ) : RAction// удаление варианта ответа

class ChangeAnswerVariant(
        val groupIndex:Int,
        val questionIndex:Int,
        val answerIndex:Int,
        val newAnswer:String,
        val newFraction:Int,
        val newFeedback:String) : RAction// изменение варианта ответа

class DeleteQuestion(
        val groupIndex:Int,
        val questionIndex:Int ) : RAction// удаление вопроса

class AddQuestion(
        val questionIndex:Int,
        val newID:Int,
        val newTitle:String,
        val newQuestion:String,
        val newType:String,
        val newUseCase:Boolean) : RAction// добавление вопроса

class CurrentQuestionToggleEdit() : RAction //переключение режима редактирования на странице с конкретным вопросом

class QuestionListToggleEdit() : RAction//переключение режима редактирования на странице со списком вопросов


//actions, использованные в лишнем компоненте, они больше не нужны
//class QuestionGroupToggleEdit() : RAction//переключение режима редактирования на странице со списком групп вопросов

//class AddQuestionGroup(val newTitle:String) : RAction// добавление группы вопросов

//class RemoveQuestionGroup(val questionGroupId:Int) : RAction//удаление группы вопросов

//class EditQuestionGroup(val questionGroupId:Int, val newTitle:String) : RAction//изменение группы вопросов

//class ImportQuestionGroup(val questions:MutableList<QuestionGroup>) : RAction// добавление группы вопросов


class ImportQuestions(val groupIndex:Int,val questions:MutableList<Question>) : RAction// добавление вопросов в группу вопросов

//class QuestionListSetEdit(val newEditable:Boolean) : RAction