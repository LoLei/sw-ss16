[![Stories in Ready](https://badge.waffle.io/LoLei/sw-ss16.png?label=ready&title=Ready)](http://waffle.io/LoLei/sw-ss16)

# sw-ss16

## Software Engineering & Knowledge Management

## App Info
Study Room Population Predicter

## Links
* [TWiki](http://www.ist.tugraz.at/software2016/bin/view/Main/WebHome)
* [Slack](https://sw-ss16.slack.com)
* [Android Ressources](https://gitlab.com/LoLei/android-repos) (Private with invitation)

## Infos
### Extreme Programming
Do everything as described in XP (TDD, PP, etc.), for further information, see below.

### Coding Standard
Use [clean code](http://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882).
If possible just follow the language's coding convention and adapt trivial styles from others.  
We can run CLions code cleanup function periodically as well.

### Git Flow / Branches
Use the [git flow workflow](http://nvie.com/posts/a-successful-git-branching-model/).  

### Issue management
Use the story cards as shown in the lecture to describe issues.

The [issue feature](https://github.com/LoLei/sw-ss16/issues) of GitHub
can be used not only for bugs, but as well as for TODO management.  
The style of our issues is that of a [Kanban board](https://en.wikipedia.org/wiki/Kanban_board).  

| Backlog  | Ready  | In progress  |  Needs review | Done   |
|:---:|:---:|:---:|:---:|:---:|
| ...  | ...  | ...  | ...  | ...  |
| ...  | ...  | ...  | ...  | ...  |

- Backlog contains all issues
- Ready marks issues which are ready for someone to start development
- In progress marks issues which are currently in development
- Needs review marks issues and PRs which are finished, but need peer review
before merging
- Done contains all finished issues  
There are issue labels for most of these columns and you can use [Waffle.io](https://waffle.io/LoLei/sw-ss16) for a real Kanban board if you want.

Tip: You can close issues from PRs with inline reference messages, for example
`Close #16 if done` (This would close issue number 16 automatically.)

### Commit message conventions
Tag each commit with the initials of the two pair programmers which worked on the commit.  
Please use the present tense or imparative mood.  
E.g.: `[LL/SR] Fix button position`  
Further reading: [1](http://chris.beams.io/posts/git-commit/#imperative), [2](https://wiki.openstack.org/wiki/GitCommitMessages)



