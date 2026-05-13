# Programming Portfolio - Student Application


*Please complete this document to confirm the work that has been done. You will also add your reflective report in the space provided*

Please replace ${\color{green}-- todo}$ with ${\color{blue}-- completed}$ once done.

Include an appropriate screenshot from your application to confirm completion. Screenshots should be added to 
the /images folder in the top-level repo.

PLEASE COMMIT TO THE MAIN BRANCH

---

### Student Application ###
(Please note that all these screens are not required - please add/remove as applicable)

|   **Topic List ${\color{blue}-- completed}$**   |   **Question Screen ${\color{blue}-- completed}$**    | **Summary Review ${\color{blue}-- completed}$**   |
|:-------------------------------------------:|:-------------------------------------------:|:-------------------------------------------:|
| ![topic list](./images/studentapp_topics.png) | ![question screen](./images/studentapp_question.png) |  ![summary review](./images/studentapp_summary.png) |


|   **Quiz History ${\color{blue}-- completed}$** |   **History Filter ${\color{blue}-- completed}$**   |   **History Detail ${\color{blue}-- completed}$**    |
|:-------------------------------------------:|:-------------------------------------------:|:-------------------------------------------:|
| ![quiz history](./images/studentapp_history.png) | ![history filter](./images/studentapp_filter.png) | ![history detail](./images/studentapp_detail.png) |


---

# REFLECTIVE REPORT

## Introduction
This report is written to reflect on the development of the Student Application, which is an Android quiz app that was built using Jetpack Compose. The app presents users with multiple-choice questions across four Android development topics, it also tracks their scores and stores quiz history using a local Room database. The development followed a step-by-step commit-driven approach, developing each features step by step from project setup through to database-backed history functionality.

The application shows core Android concepts which includes; declarative UI with Compose, navigation with NavHost, state management with ViewModel, JSON deserialization with kotlinx.serialization and persistent storage with Room. Each section below discusses the key concept, how it was applied and what was learned during the development of this Android application.

## 1. Activity and Lifecycle
The starting point of the application is MainActivity, which extends ComponentActivity. The base project was already set up as part of the starter template(commit [6b33305](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/6b33305)) and was later updated to wire in navigation system (commit [090b6ef](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/090b6ef)). The OnCreate method initialises the Compose UI using setContent, wrapping the entire application in the custom theme with dynamic colour disabled:
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentApplicationTheme(dynamicColor = false) {
                StudentApplication()
            }
        }
    }
}
```
I used enableEdgeToEdge() call which allows the app to draw behind the system bars for a modern and immersive look. However, this brought about an unexpected issue during development where the question text on the QuestionScreen was rendered beneath the status bar, which was overlapping the time and notification icons. This was resolved by adding .statusBarsPadding() to all screen layouts (commit [eacd1c0](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/eacd1c0)), which taught me how modern Android handles system window insets and why you need to account for them when using edge-to-edge rendering.
Setting dynamicColor to false was done as a deliberate decision because of lessons i learnt from the Woof codelab exercise. Dynamic colour allows the device wallpaper to override app theme colours, which means the app would look different on all device. I wanted my app to look the same everywhere, so disabling it gives me full control over the visual appearance.
Instead of having a separate Activity for each screen like the traditional approach, this app only uses one Activity that hosts everything. Unlike the traditional multi-Activity approach where each screen is a separate Activity, this application follows the single-Activity architecture pattern recommended by Google. All the screen changes are handled by the Compose Navigation library within that single Activity. This is what Google recommends because it cuts down on overhead and makes managing state easier.

## 2. Declarative UI with Jetpack Compose
Jetpack Compose fundamentally changes how Android UIs are built. Rather than creating layouts in XML files and using findViewById to control views, Compose uses composable functions that describe how the UI should look based on the current state. The framework then handles rendering and automatically updates the UI whenever the state changes. This approach eliminates an entire category of bugs related to inconsistent view state.
Every screen in the application is a composable function. The screens were created incrementally, starting with function signatures and then adding UI elements step by step. For example, the TopicListScreen was built across several commits: the initial composable with parameters (commit [4d8fcd5](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/4d8fcd5)), layout imports (commit [bbc0685](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/bbc0685)), the Column layout with title (commit [4080532](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/4080532)), and finally the LazyColumn with topic buttons (commit [f6b9a95](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/f6b9a95))
. This incremental approach made it easy to verify each change compiled correctly before moving on.
The TopicListScreen takes data and callback parameters: 

```kotlin
@Composable
fun TopicListScreen(
    topics: List<String>,
    onTopicSelected: (String) -> Unit,
    onHistoryClicked: () -> Unit
)
```
This pattern of passing data downward and events upward is called unidirectional data flow. The screen does not know where the data comes from or what happens when a button is clicked,  it just displays whatever data it's given and triggers callbacks when the user interacts with it.  This separation makes the components reusable and easier to understand because each composable only depends on what's passed into it.
Compose uses a Column for vertical layouts and Row for horizontal arrangements. Modifiers are chained together to control things like size, padding, and alignment:

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
)
```
One thing I learnt is that the order of modifiers matters. For example, .statusBarsPadding() needs to come before .padding(16.dp) so the system bar space is handled first before adding the content padding. If the order were reversed, the content padding would be applied first and then the status bar padding would push everything down further than intended. Understanding this ordering was crucial to resolving the layout issue where question text was overlapping the notification bar, and it showed that Compose modifier ordering is not for looks but has real functional implications.
The QuestionScreen (commit [0862fc0](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0862fc0)) through [13040f7](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/13040f7))  shows how Compose handles conditional rendering. Answer options are displayed as either filled Buttons or OutlinedButtons depending on whether they have been selected:
```kotlin
if (isSelected || (isAnswered && isCorrect)) {
    Button(onClick = { onAnswerSelected(index) }) {
        Text(text = "${('A' + index)}. $option")
    }
} else {
    OutlinedButton(onClick = { onAnswerSelected(index) }) {
        Text(text = "${('A' + index)}. $option")
    }
}
```
The Next button is conditionally shown only after the user has selected an answer (commit [13040f7](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/13040f7)), preventing users from skipping questions without answering.
## 3. LazyLists and Scrollable Content
For displaying lists of items, Compose has LazyColumn, which works like RecyclerView from the older View system. Unlike a regular Column that composes and renders all children immediately regardless of visibility, LazyColumn only composes and lays out items that are currently visible on screen. This lazy loading approach is essential for performance when dealing with large datasets because it avoids unnecessary memory allocation and rendering work for off-screen items.
The topic list screen uses LazyColumn with the items function (commit [f6b9a95](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/f6b9a95)):

```kotlin
LazyColumn {
    items(topics) { topic ->
        Button(
            onClick = { onTopicSelected(topic) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = topic.replaceFirstChar { it.uppercase() })
        }
    }
}
```
For the summary screen (commit [fc08707](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/fc08707)) and history detail screen (commit [040c18d](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/040c18d))
, I used itemsIndexed instead of items because I needed both the item and its position in the list. This was necessary so I could match each question to the user's answer by comparing their index positions. For example, userAnswers[index] gives me what the user selected for that specific question, and I compare it against the correct answer to decide whether to show a green or red card.:
```kotlin
LazyColumn(modifier = Modifier.weight(1f)) {
    itemsIndexed(questions) { index, question ->
        val isCorrect = userAnswers[index] == question.correctAnswerIndex
        // colour-coded Card rendering
    }
}
```
Understanding when to use items and itemsIndexed  was an important learning point. The basic items function only gives you the item itself, while itemsIndexed provides both the position and the item. The Modifier.weight(1f) on the LazyColumn ensures it takes up all available space between the header and the restart button, pushing the button to the bottom of the screen.
The history screen also uses LazyColumn (commit [22019a4](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/22019a4)) with clickable Cards to display past quiz results. Each card shows the topic name, date, and score, and clicking a card navigates to the detailed history view.
## 4. Navigation with NavHost
The app uses Jetpack Navigation for Compose to handle moving between screens. Adding the navigation dependency was one of the first setup steps (commit [2481506](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/2481506))
.  A NavHost defines all the routes the app can navigate to, and a NavController handles navigation between them.
The navigation structure was defined using a Screen enum (commit [41dda3c](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/41dda3c))
, which was later extended to include History and HistoryDetail routes (commit [db14065](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/db14065)):
```kotlin
enum class Screen {
    TopicList, Question, Summary, History, HistoryDetail
}
```
Using an enum means I reference routes as Screen.TopicList.name instead of typing out strings manually, which avoids bugs from typos. The NavHost was built incrementally: first the skeleton (commit [a5c726b](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/a5c726b)), then individual routes were added one at a time (commits [0921683](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0921683), [c385b91](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/c385b91), [886404c](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/886404c)).
Routes can include parameters passed through the URL path. The Question route passes the selected topic (commit [84b5fd9](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/84b5fd9)), and the Summary route passes score, total, and topic:
```kotlin
composable(route = Screen.Question.name + "/{topic}") { backStackEntry ->
    val topic = backStackEntry.arguments?.getString("topic") ?: ""
}
```
Back stack management is handled using popBackStack. When the user clicks Restart Quiz on the summary screen (commit [863bda1](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/863bda1)), the entire quiz back stack is cleared to return to the topic list:
```kotlin
navController.popBackStack(Screen.TopicList.name, inclusive = false)
```
Understanding the navigation back stack was one of the more challenging aspects of development. The inclusive parameter determines whether the destination itself is also removed from the stack. Setting it to false keeps the TopicList on the stack so the user lands on it, while all screens above it (Question and Summary) are removed. Without this, the user could press the system back button and return to a completed quiz, which would be a confusing experience.
The onNextClicked logic (commit [193c417](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/193c417)) demonstrates conditional navigation. If more questions remain, the ViewModel advances to the next question. If the quiz is complete, the result is saved to the database and navigation proceeds to the Summary screen.
## 5. ViewModel and State Management
The QuizViewModel extends AndroidViewModel to access the application context, which I needed for loading JSON files from the assets folder and setting up the Room database. It was created in commit [0ef0ff8](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0ef0ff8) and serves as the main state holder for the whole quiz flow.
I manage state using Compose's mutableStateOf delegate(commit [e87472d](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/e87472d)
), which automatically updates the UI whenever a value changes:
```kotlin
var currentQuestionIndex by mutableStateOf(0)
private set

var score by mutableStateOf(0)
private set

var isAnswered by mutableStateOf(false)
private set
```
The by keyword is a Kotlin delegate that lets me read and write the property like a normal variable while mutableStateOf handles the change notifications behind the scenes. The private set modifier makes sure that only the ViewModel can modify these values while external composables can read them. This enforces encapsulation and prevents screens from directly mutating application state, which would break the unidirectional data flow pattern.
User answers are tracked using mutableStateListOf (commit [38a1ac0](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/38a1ac0)
), which is a list that triggers UI updates whenever something is added or removed from it:
```kotlin
val userAnswers = mutableStateListOf<Int>()
```
The ViewModel handles quiz lifecycle through several functions, each developed and committed separately for clarity. The loadQuiz function (commit [ae451b8](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/ae451b8)
) shuffles questions and selects ten for the quiz. The selectAnswer function (commit [be24674](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/be24674)
) records the chosen answer, marks the question as answered, and increments the score if correct. The recording of the user answer was added separately (commit [0e13b12](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0e13b12)
) for the extension summary review. The nextQuestion function (commit [f996990](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/f996990)) resets the answered state and moves to the next question. This incremental development approach made debugging straightforward because I could test each piece on its own before adding more.
The ViewModel is instantiated using the viewModel() function from the Compose ViewModel library, which automatically handles the ViewModel lifecycle. It survives configuration changes and is shared across all composable routes within the NavHost, ensuring consistent state throughout the quiz flow.
## 6. Recomposition and Orientation Changes
Compose only redraws the parts of the UI that actually depend on the state that changed. When selectAnswer updates isAnswered to true, only the composables that read this value are redrawn. For example, the Next button appears because it reads isAnswered, while the title text does not recompose  because it doesn't depend on that value. This is much more efficient than the old View system where you'd have to manually redraw entire layouts.
Because the ViewModel is tied to the Activity lifecycle and not individual composables, it naturally survives things like screen rotation. This means quiz progress, scores, the current question index, and the list of user answers are all preserved when the device orientation changes without writing any additional state-saving code. In the older View system, developers would need to use onSaveInstanceState or similar mechanisms to manually save and restore all of this, which is tedious and easy to get wrong.
For the navigation filter state on the History screen (commit [ae1b0ae](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/ae1b0ae)),  I used remember to keep track of which filter is selected:
```kotlin
var selectedFilter by remember { mutableStateOf("All") }
```
If the filter needed to survive orientation changes, rememberSaveable would be used instead, as was done in the Affirmations exercise for the dialog state where the selected affirmation had to persist through orientation changes. Choosing between remember and rememberSaveable comes down to whether it's okay for the state to reset or not.
## 7. Serialization with kotlinx.serialization
The quiz questions are stored as JSON files in the assets folder, with each file corresponding to a topic. The kotlinx.serialization library was already included in the starter project.  By adding the @Serializable annotation to the data classes, the JSON gets parsed automatically without me having to extract each field manually:
```kotlin
@Serializable
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
```
The QuizRepository (commit [63ced69](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/63ced69)) reads JSON files from assets and converts them into Kotlin objects using a single function call (commit [4c1311f](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/4c1311f)):
```kotlin
fun loadQuestions(topic: String): List<Question> {
    val fileName = topic.replace(" ", "_") + ".json"
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val questionList = Json.decodeFromString<QuestionList>(jsonString)
    return questionList.questions
}
```
The topic names are derived from the JSON filenames themselves. The getTopics function lists all JSON files in the assets folder and converts filenames like activity_&_intent.json into readable topic names by removing the extension and replacing underscores with spaces. What I liked about this approach is that adding a new quiz topic only requires dropping a new JSON file into the assets folder without any code changes, which is a good example of data-driven design.

I also used serialization when saving quiz results to the Room database (commit commit [7845b76](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/7845b76)
and [7ce056d](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/7ce056d)
).  Room can't store complex objects like a list of Question objects directly, so I converted the questions into a JSON string using Json.encodeToString before saving them. For the user answers, I joined them into a comma-separated string using joinToString.
```kotlin
questionsJson = Json.encodeToString(questions),
userAnswersJson = userAnswers.joinToString(",")
```
And deserialised when viewing history details in the HistoryDetailScreen (commit [96fc40d](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/96fc40d)
):
```kotlin
val questions = Json.decodeFromString<List<Question>>(result.questionsJson)
val userAnswers = result.userAnswersJson.split(",").map { it.toInt() }
```
Using the same serialization library for both loading assets and storing data in the database kept things consistent and simple. An alternative is I could have used Room TypeConverters instead, but this approach was more straightforward since the library was already there.
## 8. Room Database and Persistence
Room sits on top of SQLite and gives you type-safe, compile-time verified database access. Setting up Room required adding the Room runtime and KTX dependencies along with the KSP (Kotlin Symbol Processing) compiler plugin (commits [25cffe3](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/25cffe3) through [20a8a5a](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/20a8a5a)). I ran into an issue early on where KSP version 2.3.0-1.0.25 didn't exist. After searching online, I found out that KSP changed its versioning for Kotlin 2.3.0 and I just needed to use version 2.3.0 on its own (commit [0ad7616](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0ad7616)
).This was a good lesson in checking compatibility between library versions.
The Room has three main components:
The Entity defines the database table structure (commit [6155d41](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/6155d41)
), i later added questionsJson and userAnswersJson fields(commit  [0beebf8](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/0beebf8)
) to store the full quiz details for history review. Each result gets an auto-generated ID as the primary key, and the rest of the fields store the topic, score, total, date, and the serialized questions and answers:
```kotlin
@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val score: Int,
    val total: Int,
    val date: String,
    val questionsJson: String = "",
    val userAnswersJson: String = ""
)
```
The DAO (Data Access Object) defines the database operations using annotations (commit [98e71ef](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/98e71ef)). This separates the database logic from the rest of the app. Room generates the actual SQL code at compile time from these annotations, which means SQL errors are caught during compilation than crashing the app at runtime:
```kotlin
@Dao
interface QuizResultDao {
    @Insert
    suspend fun insert(result: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY id DESC")
    fun getAllResults(): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results WHERE topic = :topic ORDER BY id DESC")
    fun getResultsByTopic(topic: String): Flow<List<QuizResult>>
}
```
The insert function uses suspend because writing to the database is a long-running task that shouldn't block the main thread. The query functions return Flow objects instead of plain lists, which means the UI updates automatically whenever the data in the database changes.
The Database class uses the singleton pattern so there's only ever one instance of the database across the whole app (commit [a49088e](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/a49088e)
):

```kotlin
companion object {
    @Volatile
    private var INSTANCE: QuizDatabase? = null

    fun getDatabase(context: Context): QuizDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                QuizDatabase::class.java,
                "quiz_database"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            instance
        }
    }
}
```
The @Volatile annotation makes sure that changes to the instance variable are immediately visible to all threads, and the synchronized block prevents multiple threads from creating separate database instances at the same time. I added fallbackToDestructiveMigration() when the schema changed from version 1 to version 2 to include the question and answer fields (commit [1f44467](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/1f44467)
). This approach drops and recreates the database when the schema changes, which is fine during development but in a real production app you'd need proper migration scripts to keep existing user data.

The Flow objects from the DAO work well with Compose's collectAsState, which creates a reactive UI that updates automatically when the database changes (commit [ae1b0ae](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/ae1b0ae)
):
```kotlin
val results by viewModel.getAllResults().collectAsState(initial = emptyList())
```
Saving quiz results is done asynchronously using viewModelScope.launch (commit [67cae7c](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/67cae7c)), which runs the database write on a background coroutine so it doesn't freeze the UI. The result gets saved just before the app navigates to the Summary screen (commit [bd2f50c](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/bd2f50c)).
## 9. Design Patterns
Throughout the development, I applied several design patterns that each serve a specific purpose in keeping the code organised.
**Repository Pattern**: The QuizRepository (commit [63ced69](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/63ced69)
) sits between the ViewModel and the data source, providing getTopics() and loadQuestions() methods.The ViewModel does not need to know whether data comes from JSON files, a database, or a network source. If the data source changed in the future, only the repository implementation would need updating while the rest of the application remains unchanged.
**Singleton Pattern**: The QuizDatabase uses a companion object with double-checked locking to ensure exactly one database instance exists across the application. Creating multiple database instances would waste memory and could lead to data inconsistencies.
**MVVM (Model-View-ViewModel)**: The app follows the MVVM pattern that Google recommends for Android apps. The composable screen functions act as the View, the QuizViewModel handles the state and logic, and the data classes like Question and QuizResult make up the Model. This separation means the UI can be completely redesigned without touching the logic, and the ViewModel can be unit tested without needing the Android framework.
**Unidirectional Data Flow**: State flows in one direction throughout the app, down from the ViewModel to the screens through function parameters, while user actions flow back up through callback lambdas. This makes debugging easier because any state change can be traced back to one place in the ViewModel.
**Observer Pattern**: The combination of mutableStateOf in the ViewModel and collectAsState for Room Flow objects implements the observer pattern.The UI components automatically listen for state changes and redraw themselves when the data updates, so I never have to manually tell the UI to refresh.
## 10. AI-Assisted Development
ChatGpt and Gemini AI assistants, were used throughout the development process as a guided learning tool. The AI provided some code snippet to help understand some architectural concept, it also helped in understanding how to write the structure of this essay, helped explaining the reasoning behind architectural decisions and helped diagnose and resolve issues as they came.
Key areas where AI assistance was particularly valuable included identifying the correct KSP version compatible with Kotlin 2.3.0 after the initial version caused a build failure, structuring the Room database with serialized question storage to support the history detail feature, debugging a layout issue where the LazyColumn was accidentally nested inside a Row causing the Quiz History title to render vertically (commit [ee75893](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/ee75893)), and fixing the filter dropdown menu positioning by wrapping the IconButton and DropdownMenu in a Box component (commit [c6a438a](https://github.com/comp-bkt/dma-portfolio-exercises-2026-Abiye12/commit/c6a438a)
). The AI helped explaining code snippet for deep understanding, it helped keep things short so the code can be understood and committed correctly.
## 11. Conclusion
Building this quiz app gave me hands-on experience across the full Android development stack. It covered everything from the Activity lifecycle and Compose UI at the top, through ViewModel state management and navigation in the middle, down to Room database storage and JSON serialization at the data layer.
Working in small, well-described commits taught me the importance of good version control habits. Each commit serves as documentation of what was changed and why,which makes it easy to trace how a feature developed or pinpoint when a bug was introduced. The commit history was also really useful when writing this report because I could link each concept back to the exact commit where I implemented it.
Some of the problems I ran into ended up teaching me more than the things that worked first time. Issues like the KSP version not being found, the question text overlapping the status bar, files being in the wrong package directories, and the LazyColumn rendering text vertically because it was nested inside a Row — all of these forced me to dig deeper into how Compose handles layouts, how Gradle resolves dependencies, and how Android's package system works. Fixing these problems gave me a better understanding than just following tutorials would have.
Using Compose for the UI, ViewModel for state, Navigation for screen flow, and Room for data storage is the architecture Google recommends for modern Android apps. Building each part from scratch instead of using pre-built templates helped me understand how all these libraries work together and why this approach is recommended. This foundation will be directly applicable to any future Android development work.
## 12. Bibliography
- Android Developers. (2025). Jetpack Compose. https://developer.android.com/compose

- Android Developers. (2025). Navigation in Jetpack Compose. https://developer.android.com/guide/navigation/get-started
- Android Developers. (2025). ViewModel Overview. https://developer.android.com/topic/libraries/architecture/viewmodel
- Android Developers. (2025). Save data in a local database using Room. https://developer.android.com/training/data-storage/room
- Android Developers. (2025). State and Jetpack Compose. https://developer.android.com/develop/ui/compose/state
- Kotlin Documentation. (2025). Kotlin Serialization. https://kotlinlang.org/docs/serialization.html
- Android Developers. (2025). Lists and grids. https://developer.android.com/develop/ui/compose/lists
- Android Developers. (2025). Android Activity Lifecycle. https://developer.android.com/guide/components/activities/activity-lifecycle
- Google. (2025). KSP (Kotlin Symbol Processing). https://github.com/google/ksp
- Android Developers. (2025). Material Design 3 in Compose. https://developer.android.com/develop/ui/compose/designsystems/material3
- Android Developers. (2025). Coroutines in Android. https://developer.android.com/kotlin/coroutines
- Android Developers. (2025). App Architecture Guide. https://developer.android.com/topic/architecture






