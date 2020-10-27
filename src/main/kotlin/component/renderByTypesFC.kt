package component

import hoc.withDisplayName
import react.*
import react.dom.*
import react.functionalComponent
import react.router.dom.navLink

interface WorkListProps: RProps {var questions:MutableList<IndicesWithQuestion>}

fun renderByTypes(header: String) =
		functionalComponent<WorkListProps> { props ->
			div(""){
				h2 { +header }
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
						}
					}
					tbody {
						props.questions.forEach{
							tr{
								td{
									+"${it.question.id}"
								}
								td{
									+it.question.title
								}
								td{
									+it.question.question
								}
								td{
									+it.question.type
								}
								td{
									+"${it.question.useCase}"
								}
								td{
									navLink("/question_groups/group_${it.questionGroupIndex}/${it.questionIndex}"){
										+"Подробнее >>"
									}
								}
							}
						}
					}
				}
			}
		}

fun RBuilder.renderByTypesFC(
		questions: MutableList<IndicesWithQuestion>,
		header: String
) = child(withDisplayName(header, renderByTypes(header))){
	attrs.questions = questions
}

