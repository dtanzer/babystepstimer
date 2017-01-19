using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Media;
using System.Reflection;
using System.Resources;
using System.Runtime.InteropServices;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using BabyStepTimer.Properties;

namespace BabyStepTimer
{
    static class Program
    {
        public const int WM_NCLBUTTONDOWN = 0xA1;
        public const int HTCAPTION = 0x2;
        [DllImport("User32.dll")]
        public static extern bool ReleaseCapture();
        [DllImport("User32.dll")]
        public static extern int SendMessage(IntPtr hWnd, int Msg, int wParam, int lParam);

        private static readonly String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
        private static readonly String BACKGROUND_COLOR_FAILED = "#ffcccc";
        private static readonly String BACKGROUND_COLOR_PASSED = "#ccffcc";

        private static readonly long SECONDS_IN_CYCLE = 12;

        private static System.Windows.Forms.Form timerFrame;
        private static WebBrowser timerPane;
        private static bool timerRunning;
        private static DateTime currentCycleStartTime;
        private static String lastRemainingTime;
        private static String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;

        private const string twoDigitsFormat = "00";

        [STAThread]
        static void Main()
        {
            timerFrame = new Form();
            timerFrame.Text = "Babysteps Timer";
            timerFrame.FormBorderStyle = FormBorderStyle.None;
            timerFrame.Size = new Size(250, 120);

            timerPane = new WebBrowser();
            timerPane.ScrollBarsEnabled = false;
            timerPane.DocumentText = createTimerHtml(getRemainingTimeCaption(TimeSpan.FromSeconds(0L)), BACKGROUND_COLOR_NEUTRAL, false);

            timerPane.Document.MouseDown += (sender, e) =>
            {
                var element = timerPane.Document.GetElementFromPoint(e.ClientMousePosition);
                if (element.TagName == "BODY" && e.MouseButtonsPressed == MouseButtons.Left)
                {
                    ReleaseCapture();
                    SendMessage(timerFrame.Handle, WM_NCLBUTTONDOWN, HTCAPTION, 0);
                }
            };

            timerPane.Navigating += (sender, args) =>
            {
                if (args.Url.AbsoluteUri == "command://start/")
                {
                    timerFrame.TopMost = true;
                    timerPane.Document.OpenNew(false);
                    timerPane.Document.Write(createTimerHtml(getRemainingTimeCaption(TimeSpan.FromMilliseconds(0)), BACKGROUND_COLOR_NEUTRAL, true));

                    ThreadStart start = () =>
                    {
                        timerRunning = true;
                        currentCycleStartTime = DateTime.Now;

                        while (timerRunning)
                        {
                            TimeSpan elapsedTime = DateTime.Now - currentCycleStartTime;

                            if (elapsedTime.TotalMilliseconds >= SECONDS_IN_CYCLE * 1000 + 980)
                            {
                                currentCycleStartTime = DateTime.Now;
                                elapsedTime = DateTime.Now - currentCycleStartTime;
                            }
                            if (elapsedTime.TotalMilliseconds >= 5000 && elapsedTime.TotalMilliseconds < 6000 && bodyBackgroundColor != BACKGROUND_COLOR_NEUTRAL)
                            {
                                bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
                            }

                            string remainingTime = getRemainingTimeCaption(elapsedTime);
                            if (lastRemainingTime!=remainingTime)
                            {
                                if (remainingTime == "00:10")
                                {
                                    playSound("2166__suburban-grilla__bowl-struck.wav");
                                }
                                else if (remainingTime == "00:00")
                                {
                                    playSound("32304__acclivity__shipsbell.wav");
                                    bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
                                }

                                if (timerPane.InvokeRequired)
                                {
                                    timerPane.Invoke(new Action<string>(text =>
                                    {
                                        timerPane.Document.OpenNew(false);
                                        timerPane.Document.Write(text);
                                        timerFrame.Refresh();
                                    }), createTimerHtml(remainingTime, bodyBackgroundColor, true));
                                }
                                lastRemainingTime = remainingTime;
                            }
                            Thread.Sleep(10);
                        }
                    };
                    Thread timerThread = new Thread(start);
                    timerThread.IsBackground = true;
                    timerThread.Start();
                }
                else if (args.Url.AbsoluteUri == "command://stop/")
                {
                    timerRunning = false;
                    timerFrame.TopMost = false;
                    timerPane.Document.OpenNew(false);
                    timerPane.Document.Write(createTimerHtml(getRemainingTimeCaption(TimeSpan.FromSeconds(0)), BACKGROUND_COLOR_NEUTRAL, false));
                    timerFrame.Refresh();
                }
                else if (args.Url.AbsoluteUri == "command://reset/")
                {
                    currentCycleStartTime = DateTime.Now;
                    bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
                }
                else if (args.Url.AbsoluteUri == "command://quit/")
                {
                    timerFrame.Close();
                }
                args.Cancel = true;
            };

            timerFrame.Controls.Add(timerPane);

            Application.Run(timerFrame);
        }

        private static string getRemainingTimeCaption(TimeSpan elapsedTime)
        {
            TimeSpan remainingTime = TimeSpan.FromSeconds(SECONDS_IN_CYCLE) - elapsedTime;

            long remainingMinutes = (long)remainingTime.TotalSeconds / 60;
            return remainingMinutes.ToString(twoDigitsFormat) + ":" + (remainingTime.TotalSeconds - remainingMinutes * 60).ToString(twoDigitsFormat);
        }

        private static string createTimerHtml(string timerText, string bodyColor, bool running)
        {
            string timerHtml = "<html><body style=\"border: 3px solid #555555; background: " + bodyColor +
                               "; margin: 0; padding: 0;\">" +
                               "<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">" + timerText +
                               "</h1>" +
                               "<div style=\"text-align: center\">";
            if (running)
            {
                timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> " +
                             "<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
            }
            else
            {
                timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
            }
            timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
            timerHtml += "</div>" +
                         "</body></html>";
            return timerHtml;
        }

        public static void playSound(string url)
        {
            var playThread = new Thread(() =>
            {
                var inputStream = Assembly.GetExecutingAssembly().GetManifestResourceStream("BabyStepTimer.Resources."+url);
                SoundPlayer player = new SoundPlayer(inputStream);
                player.Play();
            });
            playThread.IsBackground = true;
            playThread.Start();
        }
    }
}