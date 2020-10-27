package component

import AddAnswerVariant
import ChangeAnswerVariant
import ChangeQuestion
import RemoveAnswerVariant
import CurrentQuestionToggleEdit
import data.*
import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.functionalComponent
import store
import kotlin.browser.document

interface QuestionProps: RProps {
	var question:Question
}


fun currentQuestion(header: String, questionIndex:Int, groupIndex:Int) =
	functionalComponent<QuestionProps> { props ->
		val types = listOf("shortanswer", "numerical")

		val (typeIndex, setType) = useState(if(props.question.type == "shortanswer")0 else 1)

		val (useCase, setUseCase) = useState(props.question.useCase)

		val (lastIndex, setLastIndex) = useState(props.question.answers.size)


		div() {
			h2 { +props.question.title }
			button {
				attrs.onClickFunction = toggleEdit()

				+"Скрыть\\показать элементы редактирования"
			}
			div() {
				table(classes = "fl-table") {
					thead {
						tr {
							th{
								+"id"
							}
							th {
								+"Заголовок"
							}
							th {
								+"Вопрос"
							}
							th {
								+"Тип вопроса"
							}
							th {
								+"Строгий режим"
							}
							if(store.getState().currentQuestionEditable)
								th {
										+"Изменить"
								}
						}
					}
					tbody {
						tr{
							td{
								if(!store.getState().currentQuestionEditable)
									+"${props.question.id}"
								else
									input(InputType.number){
										attrs.id = "input-id"
										attrs.placeholder = "${props.question.id}"
									}
							}
							td{
								if(!store.getState().currentQuestionEditable)
									+props.question.title
								else
									input(InputType.text){
										attrs.id ="input-title"
										attrs.placeholder = props.question.title
									}
							}
							td{
								if(!store.getState().currentQuestionEditable)
									+props.question.question

								else
									input(InputType.text){
										attrs.id ="input-question"
										attrs.placeholder = props.question.question
									}
							}
//							if(!toggleEdit)
							td{
								if(!store.getState().currentQuestionEditable)
									+types[typeIndex]
								else
									button {
										+types[typeIndex]
										attrs.onClickFunction = {
											setType(if(typeIndex == 0) 1 else 0)
											console.log(typeIndex)
										}
									}
							}
							td{
								if(!store.getState().currentQuestionEditable)
									+"${props.question.useCase}"
								else
									button {
										+"$useCase"
										attrs.onClickFunction = {
											setUseCase(!useCase)
											console.log(useCase)
										}
									}
								}

							if(store.getState().currentQuestionEditable)
								td {
									button{
										attrs.onClickFunction = changeQuestion(groupIndex, questionIndex, types[typeIndex], useCase)

										+"Изменить"
									}
								}
							}
						}
					}
				}
			}
			h2 {
				+"Варианты ответа"
			}
				table(classes = "fl-table") {
					thead {
						tr {
							th{
								+"Инекс"
							}
							th {
								+"Вариант ответа"
							}
							th {
								+"Комментарий"
							}
							th {
								+"Баллы за ответ"
							}
							if(store.getState().currentQuestionEditable)
								th {
									+"Изменить"
								}
							if(store.getState().currentQuestionEditable)
								th {
									+"Удалить"
								}
						}
					}
					tbody {
						props.question.answers.forEachIndexed{ index, it ->
						tr{
							attrs.key = "$index"
							td{
								+"$index"
							}
							attrs.key = "$index"
							td{
								if(!store.getState().currentQuestionEditable)
									+it.answer
								else
									input(if(props.question.type == types[0]) InputType.text else InputType.number) {
										attrs.placeholder = it.answer
										attrs.id = "change_answer_input_${groupIndex}_${questionIndex}_${index}"
									}
							}
							td{
								if(!store.getState().currentQuestionEditable)
									+it.feedback
								else
									input(InputType.text) {
										attrs.placeholder = it.feedback
										attrs.id = "change_feedback_input_${groupIndex}_${questionIndex}_${index}"
									}
							}
							td{
								if(!store.getState().currentQuestionEditable)
									+"${it.fraction}"
								else
									input(InputType.number) {
										attrs.placeholder = "${it.fraction}"
										attrs.id = "change_fraction_input_${groupIndex}_${questionIndex}_${index}"
									}
							}
							if(store.getState().currentQuestionEditable)
								td {
									button {
										+"Изменить"
										attrs.onClickFunction = changeAnswerVariant(groupIndex, questionIndex, index)
									}
								}
							if(store.getState().currentQuestionEditable)
								td {
									button {
										+"Удалить"
										attrs.onClickFunction = removeAnswerVariant(groupIndex, questionIndex, index)
									}
								}
						}
					}
					if(store.getState().currentQuestionEditable)
						tr {
							td{
								+"$lastIndex"
							}
							td {
								input(if(props.question.type == types[0]) InputType.text else InputType.number) {
									attrs.placeholder = "Вариант вопроса"
									attrs.id = "answer_input"
								}
							}
							td {
								input(InputType.text) {
									attrs.placeholder = "Комментарий к ответу"
									attrs.id = "feedback_input"
								}
							}
							td {
								input(InputType.number) {
									attrs.placeholder = "Баллы за ответ"
									attrs.id = "fraction_input"
								}
							}
							td{
								button{
									+"Добавить"
									attrs.onClickFunction =  addAnswerVariant(groupIndex, questionIndex)
								}
							}
						}
				}
			}
		}

fun changeAnswerVariant(groupIndex: Int, questionIndex: Int, answerIndex: Int):(Event) -> Unit{
	return {
		store.dispatch(
			ChangeAnswerVariant(
				groupIndex,
				questionIndex,
				answerIndex,
				getInputValue("#change_answer_input_${groupIndex}_${questionIndex}_${answerIndex}"),
				getInputValue("#change_fraction_input_${groupIndex}_${questionIndex}_${answerIndex}").toInt(),
				getInputValue("#change_feedback_input_${groupIndex}_${questionIndex}_${answerIndex}")
			)
		)
	}
}

fun toggleEdit():(Event) -> Unit{
	return {
		store.dispatch(CurrentQuestionToggleEdit())
	}
}

fun removeAnswerVariant(groupIndex: Int, questionIndex: Int, answerIndex: Int):(Event) -> Unit{
	return{
		store.dispatch(
			RemoveAnswerVariant(
				groupIndex,
				questionIndex,
				answerIndex
			)
		)
	}
}

fun addAnswerVariant(groupIndex:Int, questionIndex:Int):(Event) -> Unit{

	return{
		store.dispatch(
			AddAnswerVariant(
				groupIndex,
				questionIndex,
				getInputValue("#answer_input", "0"),
				getInputValue("#fraction_input", "0").toInt(),
				getInputValue("#feedback_input")
			)
		)
	}
}

fun changeQuestion(groupIndex:Int, questionIndex:Int,newType:String, newCase:Boolean):(Event) -> Unit{
	return{
		store.dispatch(
			ChangeQuestion(
				getInputValue("#input-id").toInt(),
				groupIndex,
				questionIndex,
				getInputValue("#input-title"),
				getInputValue("#input-question"),
				newType,
				newCase
			)
		)
	}
}

fun getInputValue(inputSelector:String, defaultValue:String = ""):String  {
	val inputElement = document.querySelector(inputSelector) as HTMLInputElement
	return if(inputElement.value != "") inputElement.value
	else if (defaultValue != "") return defaultValue
	else return inputElement.placeholder
}

fun RBuilder.currentQuestionFC(
		question: Question,
		header: String,
		groupIndex:Int,
		questionIndex:Int
) = child(withDisplayName(header, currentQuestion(header, questionIndex, groupIndex))){attrs.question = question}

