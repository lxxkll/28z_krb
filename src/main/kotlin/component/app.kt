package component

import data.*
import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLUListElement
import react.*
import react.dom.*
import react.router.dom.*
import redux.*
import kotlin.browser.document

class IndicesWithQuestion(val question:Question, val questionGroupIndex:Int, val questionIndex:Int )/* Класс для группировки индексов вопроса и самого вопроса,
                                                                                                    нужен для того, чтобы в компонентах renderByTypesFC и Home работали ссылки */
interface RootProps : RProps {
	var store: Store<State, RAction, WrapperAction>
}

fun root() = functionalComponent<RootProps> { props ->
	val (visible, setVisible) = useState(true)

	header {
			nav("nav"){
				input( classes = "nav__trigger-input", type = InputType.checkBox ) {
					attrs.id = "trigger"
				}
				label( classes = "nav__trigger-finger" ) {
					attrs.htmlFor = "trigger"; span{+""}
					attrs.onClickFunction = {//при нажатии меняется состояние этого компонента, что приводит к изменению прозначности элемента с классом "nav__list"
						setVisible(!visible)
						(document.querySelector(".nav__list") as HTMLUListElement).style.setProperty("opacity", if(visible) ".85" else "0")
					}
				}
				ul("nav__list") {

					li("nav__item") {
						navLink(className = "nav__link", to = "") {
							span {
								+"домой"
							}
						}
					}
					li("nav__item") {
						navLink(className = "nav__link", to = "/question_groups") {
							span {
								+"Список вопросов"
							}
						}
					}
					li("nav__item") {
						navLink(className = "nav__link",to = "/${types[0]}"){
							span{ +"Список вопросов типа \"Short answer\"" }
						}
					}
					li("nav__item") {
						navLink(className = "nav__link",to = "/${types[1]}"){
							span{ +"Список вопросов типа \"Numerical response\"" }
						}
					}
				}
			}
		}

		switch {
//			route("/question_groups",
//					exact = true,
//					render = {
//						groupListFC(props.store.getState().questionGroup, "Список групп вопросов")//Компонент со списком всех групп вопросов
//					}
//			)
			route("/question_groups",
				exact = true,
				render = {
					currentGroupFC(props.store.getState().questionGroup[0], props.store.getState().questionGroup[0].title,0)//компонент отдельной группы вопросов
				}
			)
			route("/${types[0]}",
					exact = true,
					render = {
						renderByTypesFC(getQuestionsByType(props.store.getState().questionGroup, types[0]), "Short answer")// Компонент со всеми вопросами типа shortanswer
					}
			)
			route("/${types[1]}",
					exact = true,
					render = {
						renderByTypesFC(getQuestionsByType(props.store.getState().questionGroup, types[1]), "Number response")// Компонент со всеми вопросами типа numeric
					}
			)

			props.store.getState().questionGroup.forEachIndexed { questionGroupIndex, questionGroup ->
				route("/question_groups/group_$questionGroupIndex",
					exact = true,
					render = {
						currentGroupFC(questionGroup, questionGroup.title, questionGroupIndex)//компонент отдельной группы вопросов
					}
				)
				questionGroup.questions.forEachIndexed { index, it ->
					route("/question_groups/group_$questionGroupIndex/$index",
						exact = true,
						render = {
							currentQuestionFC(it, it.title, questionGroupIndex, index)//компонент отдельного вопроса
						}
					)
				}
			}
		}
	}

fun getQuestionsByType(questionGroup:MutableList<QuestionGroup>, type:String):MutableList<IndicesWithQuestion>{// фильтрует список по вопросам и возвращает список экземпляров класса
	val temp = mutableListOf<IndicesWithQuestion>()                                                            // IndicesWithQuestion только с вопросами указанного типа
	questionGroup.forEachIndexed {questionGroupIndex, qg ->
		qg.questions.forEachIndexed {questionIndex, question ->
			if(question.type == type)
				temp.add(IndicesWithQuestion(question, questionGroupIndex, questionIndex))
		}
	}
	return temp
}

fun RBuilder.root(store: Store<State, RAction, WrapperAction>) =
	child(withDisplayName("Root", root())) {
		attrs.store = store
	}