using System;
using System.Threading;

namespace BabyStepTimer.Tests
{
    public static class UITester
    {
        public static Lazy<string> RunTheProgram(Action testAction = null)
        {
            string html = null;
            var uiControlThread = new Thread(() =>
            {
                Thread.Sleep(100);   // Wait for the UI to initialize

                testAction?.Invoke();
                html = Program.GetCurrentHtml();

                Program.Click("quit");
            });
            uiControlThread.Start();

            Program.Main();

            return new Lazy<string>(() => html);
        }
    }
}