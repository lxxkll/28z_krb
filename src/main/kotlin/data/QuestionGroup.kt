package data

class QuestionGroup(
		var title:String,
		var questions:MutableList<Question>
) {
}

val types = listOf<String>("shortanswer", "numerical")

var counterID = 0

var questionGroupList:MutableList<QuestionGroup> = mutableListOf(
	QuestionGroup(
		title = "GROUP_1",
		questions = mutableListOf(
			Question(
				type = types[1],
				title = "Решите пример",
				question = "2+2*2",
				useCase = false,
				answers = mutableListOf(
				AnswerVariant(
					answer = "6",
					fraction = 100,
					feedback = "правильно"
				),AnswerVariant(
					answer = "8",
					fraction = 50,
					feedback = "неправильный порядок выполнения операций"
				)
				),
				id = counterID++
			),
			Question(
				type = types[0],
				title = "Ответьте на вопрос по истории",
				question = "Первый правитель древнерусского государства",
				useCase = true,
				answers = mutableListOf(
				AnswerVariant(
					answer = "Рюрик",
					fraction = 100,
					feedback = "правильно"
				),AnswerVariant(
					answer = "рюрик",
					fraction = 90,
					feedback = "Правильно, но имена пишутся с большой буквы"
				),AnswerVariant(
					answer = "Олег",
					fraction = 50,
					feedback = "Он был вторым"
				)
				),
				id = counterID++
			),
			Question(
				type = types[0],
				title = "Укажите перевод на русский",
				question = "\"instance\"",
				useCase = false,
				answers = mutableListOf(
					AnswerVariant(
						answer = "Экземпляр",
						fraction = 100,
						feedback = "правильно"
					)
				),
				id = counterID++
			),
			Question(
				type = types[1],
				title = "решите уравнение",
				question = "9*x^2-6*x+1==0",
				useCase = true,
				answers = mutableListOf(
				AnswerVariant(
					answer = "1/3",
					fraction = 100,
					feedback = "правильно"
				),AnswerVariant(
					answer = "6/8",
					fraction = 90,
					feedback = "правильно"
				),AnswerVariant(
					answer = "0.33",
					fraction = 50,
					feedback = "правильно"
				)
				),
				id = counterID++
			),
			Question(
				type = types[0],
				title = "Ответьте на вопрос",
				question = "Что такое гипотенуза ",
				useCase = false,
				answers = mutableListOf(
					AnswerVariant(
						answer = "Самая длинная сторона прямоугольного треугольника",
						fraction = 100,
						feedback = "правильно"
					)
				),
				id = counterID++
			)
		)
	)
)
