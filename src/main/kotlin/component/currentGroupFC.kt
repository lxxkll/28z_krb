package component

import AddQuestion
import DeleteQuestion
import ImportQuestions
import QuestionListToggleEdit
import data.*
import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.functionalComponent
import react.router.dom.navLink
import store
import kotlin.browser.document

interface GroupProps: RProps {
	var questions:QuestionGroup
}
external fun encodeURIComponent(str: String): String

fun currentGroup(header: String, index:Int, type: Int) =
	functionalComponent<GroupProps> { props ->
		val types = listOf("shortanswer", "numerical")

		val (typeIndex, setType) = useState(0)
		val result = questionGroupXML(store.getState().questionGroup[index])

		val (visibleImport, setVisibleImport) = useState(false)
		val (visibleExport, setVisibleExport) = useState(false)
		val (visibleExportFormat, setVisibleExportFormat) = useState(false)
		val (case, setCase) = useState(false)
		div() {

			div() {
				h2 { +header }
				button {
					attrs.onClickFunction = questionListToggleEdit()
					+"Скрыть\\показать элементы редактирования"
				}
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
							th {
								+"Ссылка"
							}
							if(store.getState().questionListEditable) // если не была нажата кнопка редактирования, то эта ячейка отрисовываться не будет
								th {
									+"Удалить"
								}
						}
					}
					tbody {
						props.questions.questions.forEachIndexed{ questionIndex, it ->
							if(type == 2 || it.type == types[type])
							tr{
								td{
									+"${it.id}"
								}
								td{
									+it.title
								}
								td{
									+it.question
								}
								td{
									+it.type
								}
								td{
									+"${it.useCase}"
								}
								td{
									navLink("/question_groups/group_$index/$questionIndex"){
										+"Подробнее >>"
									}
								}
								if(store.getState().questionListEditable)
									td{
										button(classes = "btn-2") {
											+"Удалить"
											attrs.onClickFunction = deleteQuestion(index, questionIndex)
										}
									}
							}

						}

						if(store.getState().questionListEditable)
							tr{
								td{
									input(InputType.number) {
										attrs.placeholder = "id"
										attrs.id = "add-id"
									}
								}
								td{
									input(InputType.text) {
										attrs.placeholder = "title"
										attrs.id = "add-title"
									}
								}
								td{
									input(InputType.text) {
										attrs.placeholder = "question"
										attrs.id = "add-question"
									}
								}
								td{
									button(classes = "btn-2"){
										+types[typeIndex]
										attrs.onClickFunction = {
											setType(if(typeIndex == 0) 1 else 0)
										}
									}
								}
								td{
									button(classes = "btn-2"){
										+"$case"
										attrs.onClickFunction = {
											setCase(!case)
										}
									}
								}
								td{
									button {
										+"Добавить"
										attrs.onClickFunction = addQuestion(index, types[typeIndex], case)
									}
								}
							}
					}
				}
				if(type == 2)
				div("export-import"){
					div("exportBlock"){
						button {
							+"Экспорт"
							attrs.onClickFunction = {setVisibleExport(!visibleExport)}
						}
						if(visibleExport){
							a {
								attrs.href =
									"data:application/text; charset=utf-8," + encodeURIComponent("<xml version=\"1.0\" />\n \n $result")
								button { +"Скачать" }
							}
							button {
								+"Показать/скрыть вопросы в moodle xml"
								attrs.onClickFunction = { setVisibleExportFormat(!visibleExportFormat) }
							}
							if (visibleExportFormat) pre {
								+result
							}
						}
					}
					div(classes = "inputBlock") {
						button {
							+"Импорт"
							attrs.onClickFunction = {setVisibleImport(!visibleImport)}
						}
						if(visibleImport){
							textArea {
								attrs.id = "input-import"
								attrs.placeholder = "Вставьте moodleXML сюда"
							}
							button {
								attrs.onClickFunction = submitInput(index)
								+"Импортировать"
							}
						}
					}
				}
			}
		}
	}
//экспорт
fun answersXML(answers:MutableList<AnswerVariant>):String{
	var result = ""
	answers.forEach {
		result +="<answer fraction=\"${it.fraction}\">\n" +
				"\t" +"\t" +	"<text>\n" +
				"\t" +"\t" +"\t" +		it.answer +"\n"+
				"\t" +"\t" +	"</text>\n" +
				"\t" +"\t" +		"<feedback>\n" +
				"\t" +"\t" +		"<text>\n" +
				"\t" +"\t" +"\t" +			it.feedback + "\n"+
				"\t" +"\t" +	"</text>\n" +
				"\t" +"\t" +	"</feedback>\n" +
				"\t" +"</answer>\n"
	}
	return result
}

fun questionXML(questionGroup:MutableList<Question>):String{
	var result = ""
	questionGroup.forEach{
		result +=   "\t" +"<question type = \"${it.type}\">\n" +
				"\t" +	"<name>\n"+
				"\t" +"\t" +	"<text>\n${it.title}\n</text>\n"+
				"\t" +	"</name>\n"+
				"\t" +"<questiontext format=\"html\">\n" +
				"\t" +"\t" +	it.question + "\n"+
				"\t" +"</questiontext>\n" +
				"\t" + answersXML(it.answers) +"\n"+
				"\t" +"</question>\n"
	}
	return result
}
fun questionGroupXML(questionGroup:QuestionGroup):String{
	return "<quiz>\n" +
				questionXML(questionGroup.questions)+"\n"+
			"</quiz>\n"
}

//импорт
fun getFormatAnswers(input:String):MutableList<AnswerVariant>{
	var temp = input
	val answersFormat = mutableListOf<AnswerVariant>()
	while( "-1" != temp.resSubstringAfter("<answer fraction").resSubstringBefore("</answer>")) {
		answersFormat.add(AnswerVariant(
			answer = temp.resSubstringAfter("<answer fraction").resSubstringBefore("</answer>").resSubstringAfter("<text>").resSubstringBefore("</text>").replace("\n","").replace("\t",""),
			feedback = temp.resSubstringAfter("<answer fraction").resSubstringBefore("</answer>").resSubstringAfter("<feedback>").resSubstringBefore("</feedback>").resSubstringAfter("<text>").resSubstringBefore("</text>").replace("\n","").replace("\t",""),
			fraction = temp.resSubstringAfter("<answer fraction=\"").resSubstringBefore("\"").toInt()
		))
		temp = temp.removeRange(temp.indexOf("<answer fraction")..temp.indexOf("</answer>"))
	}
	return answersFormat
}
fun getFormatQuestions(input:String):MutableList<Question>{
	var temp = input
	val questions = mutableListOf<String>()
	val res = mutableListOf<Question>()
	var counter = 1
	while( "-1" != temp.resSubstringAfter("<question type").resSubstringBefore("</question>")){
		res.add(Question(
			type = temp.resSubstringAfter("<question type=\"").resSubstringBefore("\">").replace("\n","").replace("\t",""),
			title = temp.resSubstringAfter("<name>").resSubstringBefore("</name>").resSubstringAfter("<text>").resSubstringBefore("</text>").replace("\n","").replace("\t",""),
			question = temp.resSubstringAfter("<questiontext format=\"html\">").resSubstringBefore("</questiontext>").replace("\n","").replace("\t",""),
			useCase = false,
			answers = getFormatAnswers(temp.resSubstringAfter("<question type").resSubstringBefore("</question>")),
			id = store.getState().questionGroup.size + counter++
		))
		questions.add(temp.resSubstringAfter("<question type").resSubstringBefore("</question>"))
		temp  = temp.removeRange(temp.indexOf("<question type")..temp.indexOf("</question>"))
	}
	return res
}
fun getFormatQuestionGroup(input:String):MutableList<Question>{
	var temp = input
	val quizs = mutableListOf<String>()
	val result = mutableListOf<QuestionGroup>()
	while( "-1" != temp.resSubstringAfter("<quiz>").resSubstringBefore("</quiz>")){
		quizs.add(temp.resSubstringAfter("<quiz>").resSubstringBefore("</quiz>"))
		temp  = temp.removeRange(temp.indexOf("<quiz>")..temp.indexOf("</quiz>"))
	}
	quizs.forEach {
		result.add(QuestionGroup("IMPORTED",getFormatQuestions(it)))
	}
	return result[0].questions
}
fun submitInput(index:Int):(Event) -> Unit{
	return {
		store.dispatch(ImportQuestions(index,getFormatQuestionGroup((document.querySelector("#input-import") as HTMLTextAreaElement).value)))
	}
}

fun String.resSubstringAfter(delimiter: String, missingDelimiterValue: String = this): String {
	val index = indexOf(delimiter)
	return if (index == -1) "-1" else substring(index + delimiter.length, length)
}
fun String.resSubstringBefore(delimiter: String, missingDelimiterValue: String = this): String {
	val index = indexOf(delimiter)
	return if (index == -1) "-1" else substring(0, index)
}
fun addQuestion(
	questionGroupIndex:Int,
	newType:String,
	newUseCase:Boolean):(Event) -> Unit{
	return {
		store.dispatch(AddQuestion(
			questionGroupIndex,
			getInputValue("#add-id",(store.getState().questionGroup[questionGroupIndex].questions.last().id+1).toString()).toInt(),
			getInputValue("#add-title"),
			getInputValue("#add-question"),
			newType,
			newUseCase
		))
	}
}

fun deleteQuestion(groupIndex:Int, questionIndex:Int):(Event) -> Unit{
	return {
		store.dispatch(DeleteQuestion(groupIndex, questionIndex))
	}
}

fun questionListToggleEdit():(Event) -> Unit{
	return {
		store.dispatch(QuestionListToggleEdit())
	}
}

fun RBuilder.currentGroupFC(
		questions: QuestionGroup,
		header: String,
		indexGroup:Int,
		type:Int
) = child(withDisplayName(header, currentGroup(header, indexGroup, type))){attrs.questions = questions}
