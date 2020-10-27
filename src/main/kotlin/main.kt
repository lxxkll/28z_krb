import component.root
import data.*
import react.dom.render
import react.router.dom.hashRouter
import redux.*
import wrapper.reduxLogger
import kotlin.browser.document

val store = createStore(
        ::changeReducer,
        State(questionGroupList, currentQuestionEditable = false, questionListEditable = false, questionGroupEditable = false),
        compose(
                rEnhancer(),
                applyMiddleware(
                        reduxLogger.logger as Middleware<State, Action, Action, Action, Action>
                )
        )
)

val rootDiv = document.getElementById("root")

fun render() = render(rootDiv) {
    hashRouter {
        root(store)
    }
}

fun main() {
    console.log(questionGroupList)
    render()
    store.subscribe {
        render()
    }
}