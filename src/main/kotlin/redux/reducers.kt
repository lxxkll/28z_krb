package redux

import AddAnswerVariant
import AddQuestion
//import AddQuestionGroup
import ChangeAnswerVariant
import ChangeQuestion
import RemoveAnswerVariant
import CurrentQuestionToggleEdit
import DeleteQuestion
//import QuestionGroupToggleEdit
import QuestionListToggleEdit
//import RemoveQuestionGroup
//import EditQuestionGroup
import ImportQuestions
import data.*

fun changeReducer(state: State, action: RAction) =
		when (action) {
			is ChangeQuestion -> State(
					getWithNewQuestion(questionGroupList,
							action.newID,
							action.groupIndex,
							action.questionIndex,
							action.newTitle,
							action.newQuestion,
							action.newType,
							action.newCase),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
			is AddAnswerVariant -> State(
					getWithNewAnswerVariant(
							state.questionGroup,
							action.groupIndex,
							action.questionIndex,
							action.newAnswer,
							action.newFraction,
							action.newFeedback
					),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
			is RemoveAnswerVariant -> State(
					getWithRemovedAnswerVariant(
							state.questionGroup,
							action.groupIndex,
							action.questionIndex,
							action.answerIndex
					),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
			is CurrentQuestionToggleEdit -> State(
					state.questionGroup,
					!state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)

			is ChangeAnswerVariant -> State(
					getWithChangedAnswerVariant(
							state.questionGroup,
							action.groupIndex,
							action.questionIndex,
							action.answerIndex,
							action.newAnswer,
							action.newFraction,
							action.newFeedback
					),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
			is QuestionListToggleEdit -> State(
					state.questionGroup,
					state.currentQuestionEditable,
					!state.questionListEditable,
					state.questionGroupEditable
			)
			is DeleteQuestion -> State(
					getWithDeletedQuestion(state.questionGroup, action.groupIndex, action.questionIndex),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
			is AddQuestion -> State(
					getWithNewQuestion(state.questionGroup,
							action.questionIndex,
							action.newID,
							action.newTitle,
							action.newQuestion,
							action.newType,
							action.newUseCase),
					state.currentQuestionEditable,
					state.questionListEditable,
					state.questionGroupEditable
			)
//			is QuestionGroupToggleEdit -> State(
//					state.questionGroup,
//					state.currentQuestionEditable,
//					state.questionListEditable,
//					!state.questionGroupEditable
//			)
//			is AddQuestionGroup -> State(
//					getWithNewQuestionGroup(state.questionGroup,action.newTitle),
//					state.currentQuestionEditable,
//					state.questionListEditable,
//					state.questionGroupEditable
//			)
//			is RemoveQuestionGroup -> State(
//					getWithRemovedQuestionGroup(state.questionGroup,action.questionGroupId),
//					state.currentQuestionEditable,
//					state.questionListEditable,
//					state.questionGroupEditable
//			)
//			is EditQuestionGroup -> State(
//					getWithEditedQuestionList(state.questionGroup, action.questionGroupId, action.newTitle),
//					state.currentQuestionEditable,
//					state.questionListEditable,
//					state.questionGroupEditable
//			)
//			is ImportQuestionGroup -> State(
//					getWithImportedQuestionGroup(state.questionGroup, action.questions),
//					state.currentQuestionEditable,
//					state.questionListEditable,
//					state.questionGroupEditable
//			)
			is ImportQuestions -> State(
				getWithImportedQuestions(state.questionGroup, action.groupIndex,action.questions),
				state.currentQuestionEditable,
				state.questionListEditable,
				state.questionGroupEditable
			)
			else -> state
		}
fun getWithImportedQuestions(questionGroupList:MutableList<QuestionGroup>,
							 groupIndex:Int,
							 questions:MutableList<Question>):MutableList<QuestionGroup>{//Добавляет импортированные вопросы
	return questionGroupList.apply {
		questions.forEach {
			this[groupIndex].questions.add(it.apply { this.id =  questionGroupList[0].questions.last().id+1})
		}
	}
}
//fun getWithImportedQuestionGroup(questionGroupList:MutableList<QuestionGroup>,
//                                 questions:MutableList<QuestionGroup>):MutableList<QuestionGroup>{//Добавляет импортированные группы вопросов
//	return questionGroupList.apply {
//		questions.forEachIndexed { index, questionGroup ->
//			this.add(QuestionGroup(questionGroup.title,questionGroup.questions))
//
//		}
//	}
//}
//fun getWithEditedQuestionList(questionGroupList:MutableList<QuestionGroup>,
//                              questionGroupId:Int, newTitle: String):MutableList<QuestionGroup>{//возвращает список групп вопросов с измененным списком вопросов
//	return questionGroupList.apply {
//		this[questionGroupId].title = newTitle
//	}
//}
//
//fun getWithRemovedQuestionGroup(questionGroupList:MutableList<QuestionGroup>,
//                                questionGroupId:Int):MutableList<QuestionGroup>{//возвращает список групп вопросов с удаленной группой вопросов
//	return questionGroupList.apply {
//		this.removeAt(questionGroupId)
//	}
//}
//
//fun getWithNewQuestionGroup(questionGroupList:MutableList<QuestionGroup>,
//                            newTitle:String):MutableList<QuestionGroup>{//возвращает список групп вопросов с добавленной группой вопросов
//	return questionGroupList.apply {
//		this.add(QuestionGroup(
//				newTitle,
//				mutableListOf(
//						Question(
//								types[0],
//								"Введите заголовок ответа",
//								"Введите вопрос",
//								false,
//								mutableListOf(
//										AnswerVariant(
//												"Введите вариант ответа",
//												0,
//												"Введите комментарий к ответу"
//										)
//								),
//								if(this.lastIndex > 0 /*&& this[this.lastIndex].questions.lastIndex > 0*/) this[this.lastIndex].questions[this[this.lastIndex].questions.lastIndex].id else 1
//						)
//				)
//		))
//	}
//}

fun getWithNewQuestion(questionGroupList:MutableList<QuestionGroup>,
                       questionIndex:Int,
                       newID:Int,
                       newTitle:String,
                       newQuestion:String,
                       newType:String,
                       newUseCase:Boolean):MutableList<QuestionGroup>{//возвращает список групп вопросов с
	return questionGroupList.apply {
		this[questionIndex].questions.add(
				Question(
						newType,
						newTitle,
						newQuestion,
						newUseCase,
						mutableListOf(AnswerVariant(
								"Введите вариант ответа",
								0,
								"Введите комментарий к ответу"
						)),
						newID
				)
		)
	}
}

fun getWithDeletedQuestion(questionGroupList:MutableList<QuestionGroup>,
                           groupIndex:Int,
                           questionIndex:Int):MutableList<QuestionGroup>{//возвращает список групп вопросов с удаленным вопросом
	return if(questionGroupList[groupIndex].questions.size > 1)
		questionGroupList.apply {

			this[groupIndex].questions.removeAt(questionIndex)
		}
	else questionGroupList
}

fun getWithChangedAnswerVariant(questionGroupList:MutableList<QuestionGroup>,
                                groupIndex:Int,
                                questionIndex:Int,
                                answerIndex:Int,
                                newAnswer:String,
                                newFraction:Int,
                                newFeedback:String):MutableList<QuestionGroup>{//возвращает список групп вопросов с измененным вариантом ответа
	return questionGroupList.apply {
		this[groupIndex].questions[questionIndex].answers[answerIndex].apply {
			this.answer = newAnswer
			this.fraction = newFraction
			this.feedback = newFeedback
		}
	}
}

fun getWithRemovedAnswerVariant(questionGroupList:MutableList<QuestionGroup>,
                                groupIndex:Int,
                                questionIndex:Int,
                                answerIndex:Int):MutableList<QuestionGroup>{//возвращает список групп вопросов судаленным вариантом ответа
	return questionGroupList.apply {
		this[groupIndex].questions[questionIndex].answers.removeAt(answerIndex)
	}
}

fun getWithNewAnswerVariant(questionGroupList:MutableList<QuestionGroup>,
                            groupIndex:Int,
                            questionIndex:Int,
                            newAnswer:String,
                            newFraction:Int,
                            newFeedback:String):MutableList<QuestionGroup>{//возвращает список групп вопросов с новым вариантом ответа
	return questionGroupList.apply {
		this[groupIndex].questions[questionIndex].answers.add(
				AnswerVariant(
						newAnswer,
						newFraction,
						newFeedback
				)
		)
	}
}

fun getWithNewQuestion(questionGroupList:MutableList<QuestionGroup>,
                       newID:Int,
                       groupIndex:Int,
                       questionIndex:Int,
                       newTitle:String,
                       newQuestion:String,
                       newType:String,
                       newCase:Boolean):MutableList<QuestionGroup>{//возвращает список групп вопросов с новым вопросом
	return questionGroupList.apply {
		this[groupIndex].questions[questionIndex].apply {
			this.id = newID
			this.title = newTitle
			this.question = newQuestion
			this.type = newType
			this.useCase = newCase
		}
	}
}