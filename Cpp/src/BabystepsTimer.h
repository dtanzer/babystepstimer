#pragma once

#include <QObject>

#include <chrono>
#include <string>

class BabystepsTimer : public QObject {

  Q_OBJECT

public:
  int exec(int argc, char * argv[]);

signals:
  void updateGui(QString const & text);

private:
  using Clock = std::chrono::steady_clock;
  using TimePoint = Clock::time_point;

  void timerThread();

  static std::string getRemainingTimeCaption(int elapsedTime);
  static std::string createTimerHtml(std::string timerText, std::string bodyColor, bool running);
};
