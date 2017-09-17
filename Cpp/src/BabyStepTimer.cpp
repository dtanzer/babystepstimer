#include <QtWidgets/QWidget>
#include <QtWidgets/QApplication>
#include <QtWidgets/QTextEdit>
#include <QtWidgets/QTextBrowser>

#include <string>

static const long SECONDS_IN_CYCLE = 120;

static const std::string BACKGROUND_COLOR_NEUTRAL = "#ffffff";
static const std::string BACKGROUND_COLOR_PASSED = "#ccffcc";
static const std::string BACKGROUND_COLOR_FAILED = "#ffcccc";

static const auto twoDigitsFormat = "%02d";

static std::string getRemainingTimeCaption(long elapsedTime)
{
  long elapsedSeconds = elapsedTime/1000;
  long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

  long remainingMinutes = remainingSeconds/60;

  char remainingMinutesBuffer[4];
  std::sprintf(remainingMinutesBuffer, twoDigitsFormat, remainingMinutes);
  char remainingSecondsBuffer[8];
  std::sprintf(remainingSecondsBuffer, twoDigitsFormat, remainingSeconds-remainingMinutes*60);

  return std::string(remainingMinutesBuffer) + ":" + std::string(remainingSecondsBuffer);
}

static std::string createTimerHtml(std::string timerText, std::string bodyColor, bool running)
{
  using namespace std::string_literals;

  std::string timerHtml =
    "<html><body style=\"border: 3px solid #555555; background: " + bodyColor + "; margin: 0; padding: 0;\">" +
    "<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">" + timerText + "</h1>" +
    "<div style=\"text-align: center\">";
  if(running) {
    timerHtml +=
      "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> "s +
      "<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
  } else {
    timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
  }
  timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
  timerHtml += "</div>";
  timerHtml += "</body></html>";
  return timerHtml;
}

int main(int argc, char * argv[]) {
  QApplication application{argc, argv};
  QWidget window;

  window.setFixedSize(250, 120);

  QTextBrowser timerWidget;
  timerWidget.setParent(&window);

  timerWidget.setOpenLinks(false);
  timerWidget.setReadOnly(true);

  QObject::connect(&timerWidget, &QTextBrowser::anchorClicked, [&](QUrl url){
    if(url.url() == "command://start") {

    } else if(url.url() == "command::stop") {

    } else if(url.url() == "command://reset") {

    } else if(url.url() == "command://quit") {
      exit(0);
    }

  });

  timerWidget.setHtml(QString::fromStdString(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, false)));

  window.show();
  return application.exec();
}