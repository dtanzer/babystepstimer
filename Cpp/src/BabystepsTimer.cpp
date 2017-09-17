#include "BabystepsTimer.h"

#include <QtMultimedia/QSound>
#include <QtWidgets/QApplication>
#include <QtWidgets/QTextBrowser>

#include <thread>

static const long SECONDS_IN_CYCLE = 120;

static const std::string BACKGROUND_COLOR_NEUTRAL = "#ffffff";
static const std::string BACKGROUND_COLOR_PASSED = "#ccffcc";
static const std::string BACKGROUND_COLOR_FAILED = "#ffcccc";

static const auto twoDigitsFormat = "%02d";

static bool timerRunning;
static std::chrono::steady_clock::time_point currentCycleStartTime;
static std::string lastRemainingTime;
static std::string bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;

int BabystepsTimer::exec(int argc, char * argv[]) {
  QApplication application{argc, argv};
  QWidget window;

  window.setFixedSize(250, 120);

  QTextBrowser timerWidget;
  timerWidget.setParent(&window);
  timerWidget.setOpenLinks(false);
  timerWidget.setReadOnly(true);

  QObject::connect(this, &BabystepsTimer::updateGui, this, [&](QString const & text) { timerWidget.setHtml(text); });
  QObject::connect(this, &BabystepsTimer::playSound, this, [&](QString const & filename) { QSound::play(filename); });

  QObject::connect(&timerWidget, &QTextBrowser::anchorClicked, [&](QUrl url) {
    if(url.url() == "command://start") {
      timerWidget.setHtml(
        QString::fromStdString(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, true)));
      std::thread([this] { timerThread(); }).detach();
    } else if(url.url() == "command://stop") {
      timerRunning = false;
      timerWidget.setHtml(
        QString::fromStdString(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, false)));
    } else if(url.url() == "command://reset") {
      currentCycleStartTime = Clock::now();
      bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
    } else if(url.url() == "command://quit") {
      exit(0);
    }
  });

  timerWidget.setHtml(
    QString::fromStdString(createTimerHtml(getRemainingTimeCaption(0L), BACKGROUND_COLOR_NEUTRAL, false)));

  window.show();
  return application.exec();
}

std::string BabystepsTimer::getRemainingTimeCaption(int elapsedTime) {
  long elapsedSeconds = elapsedTime / 1000;
  long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

  long remainingMinutes = remainingSeconds / 60;

  char remainingMinutesBuffer[4];
  std::sprintf(remainingMinutesBuffer, twoDigitsFormat, remainingMinutes);
  char remainingSecondsBuffer[8];
  std::sprintf(remainingSecondsBuffer, twoDigitsFormat, remainingSeconds - remainingMinutes * 60);

  return std::string(remainingMinutesBuffer) + ":" + std::string(remainingSecondsBuffer);
}

std::string BabystepsTimer::createTimerHtml(std::string timerText, std::string bodyColor, bool running) {
  using namespace std::string_literals;

  std::string timerHtml = "<html><body style=\"border: 3px solid #555555; background: " + bodyColor
                          + "; margin: 0; padding: 0;\">"
                          + "<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">" + timerText + "</h1>"
                          + "<div style=\"text-align: center\">";
  if(running) {
    timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> "s
                 + "<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
  } else {
    timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
  }
  timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
  timerHtml += "</div>";
  timerHtml += "</body></html>";
  return timerHtml;
}

void BabystepsTimer::timerThread() {
  timerRunning = true;
  currentCycleStartTime = Clock::now();

  while(timerRunning) {
    auto elapsedTime = std::chrono::duration_cast<std::chrono::milliseconds>(Clock::now() - currentCycleStartTime);

    if(elapsedTime >= std::chrono::seconds(SECONDS_IN_CYCLE) + std::chrono::milliseconds(980)) {
      currentCycleStartTime = Clock::now();
      elapsedTime = std::chrono::duration_cast<std::chrono::milliseconds>(Clock::now() - currentCycleStartTime);
    }

    if(elapsedTime >= std::chrono::milliseconds(5000) && elapsedTime < std::chrono::milliseconds(6000)
       && BACKGROUND_COLOR_NEUTRAL != bodyBackgroundColor) {
      bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
    }

    std::string remainingTime = getRemainingTimeCaption(elapsedTime.count());
    if(remainingTime != lastRemainingTime) {
      if(remainingTime == "00:10") {
        emit playSound("2166__suburban-grilla__bowl-struck.wav");
      } else if(remainingTime == "00:00") {
        emit playSound("32304__acclivity__shipsbell.wav");
        bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
      }
      emit updateGui(QString::fromStdString(createTimerHtml(remainingTime, bodyBackgroundColor, true)));
      lastRemainingTime = remainingTime;
    }
    std::this_thread::sleep_for(std::chrono::milliseconds(10));
  }
}
