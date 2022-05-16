# Cat Facts


# Introduction
This is a sample android app with to know more about cat related to facts

# Project Configuration
Language: Kotlin\
Minimum SDK: 22

# Project Structure
app
- MainActivity for loading cat related facts
- ViewModel with function to fetch remote results data from Repository(remote and db)
- ViewImpl class maintains the view releated logics
- Other components separated for easy unit testing and maintenance

# Tech Stack
Kotlin\
Android\
MVVM\
Koin for dependency injection\
Retrofit for networking\
Moshi for json parsing\.\
Coroutines for async tasks.\
JUnit5 for unit test\
Mockk for mocking\
Room for DB
