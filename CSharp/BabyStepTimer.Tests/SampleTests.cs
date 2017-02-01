using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace BabyStepTimer.Tests
{
    [TestClass]
    public class SampleTests
    {
        [TestMethod]
        public void AfterStartup_ShouldShowTwoMinutes()
        {
            var html = UITester.RunTheProgram();

            StringAssert.Contains(html.Value, "2:00");
        }

        [TestMethod]
        public void AfterStartup_ShouldOfferStartCommand()
        {
            var html = UITester.RunTheProgram();

            StringAssert.Contains(html.Value, "command://start");
        }

        [TestMethod]
        public void ASecondAfterClickingStart_ShouldShow1Minute59Seconds()
        {
            var html = UITester.RunTheProgram(() =>
            {
                // Act
                Program.Click("start");
                Thread.Sleep(1100);
            });

            StringAssert.Contains(html.Value, "1:59");
        }
    }
}
