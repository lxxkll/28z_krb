package data

class State (
    var questionGroup:MutableList<QuestionGroup>,//список групп вопросов
    var currentQuestionEditable:Boolean,//показаны или скрыты элементы редактирования в списке отдельного вопроса
    var questionListEditable:Boolean,//показаны или скрыты элементы редактирования в списке вопросов
    var questionGroupEditable:Boolean//показаны или скрыты элементы редактирования в списке групп вопросов
)