#pragma once

#include <QObject>

#include <string>

class BabystepsTimer : public QObject {

Q_OBJECT

private:

  static std::string getRemainingTimeCaption(long elapsedTime);
  static std::string createTimerHtml(std::string timerText,
                                     std::string bodyColor, bool running);
signals:
  void updateGui(std::string const & text);

public:
  int exec(int argc, char *argv[]);
};

