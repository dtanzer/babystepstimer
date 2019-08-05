import AVFoundation
import UIKit

final class ViewController: UIViewController {
    @IBOutlet private var timerLabel: UILabel!
    @IBOutlet private var resetButton: UIButton!
    @IBOutlet private var startStopButton: UIButton!
    private var isTimerRunning = false
    private var timer: Timer?
    private let secondsInCycle = 120
    private var elapsedSeconds = 0
    private var warningSoundPlayer: AVAudioPlayer!
    private var failureSoundPlayer: AVAudioPlayer!

    override func viewDidLoad() {
        super.viewDidLoad()
        timerLabel.text = remainingTime(elapsedSeconds: 0)
        resetButton.isEnabled = false
        let warningSoundPath = Bundle.main.path(forResource: "2166__suburban-grilla__bowl-struck", ofType: "wav")!
        warningSoundPlayer = try! AVAudioPlayer(contentsOf: URL(fileURLWithPath: warningSoundPath))
        warningSoundPlayer.prepareToPlay()
        let failureSoundPath = Bundle.main.path(forResource: "32304__acclivity__shipsbell", ofType: "wav")!
        failureSoundPlayer = try! AVAudioPlayer(contentsOf: URL(fileURLWithPath: failureSoundPath))
        failureSoundPlayer.prepareToPlay()
    }

    @IBAction private func startStop() {
        if !isTimerRunning {
            isTimerRunning = true
            startStopButton.setTitle("Stop", for: .normal)
            resetButton.isEnabled = true
            startTimer()
            view.backgroundColor = .white
        } else {
            isTimerRunning = false
            startStopButton.setTitle("Start", for: .normal)
            resetButton.isEnabled = false
            timer?.invalidate()
            warningSoundPlayer.pause()
            warningSoundPlayer.currentTime = 0
            failureSoundPlayer.pause()
            failureSoundPlayer.currentTime = 0
        }
    }

    @IBAction private func reset() {
        timerLabel.text = remainingTime(elapsedSeconds: 0)
        timer?.invalidate()
        startTimer()
        view.backgroundColor = .green
        warningSoundPlayer.pause()
        warningSoundPlayer.currentTime = 0
        failureSoundPlayer.pause()
        failureSoundPlayer.currentTime = 0
    }

    private func startTimer() {
        timerLabel.text = remainingTime(elapsedSeconds: 0)
        elapsedSeconds = 0
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            self.elapsedSeconds += 1
            let remainingTime = self.remainingTime(elapsedSeconds: self.elapsedSeconds)
            if remainingTime == "0:10" {
                self.warningSoundPlayer.play()
            } else if remainingTime == "0:00" {
                self.failureSoundPlayer.play()
                self.view.backgroundColor = .red
            } else if remainingTime.first != "-" {
                self.view.backgroundColor = .white
            }
            self.timerLabel.text = remainingTime
        }
    }

    private func remainingTime(elapsedSeconds: Int) -> String {
        let remainingSeconds = self.secondsInCycle - elapsedSeconds
        let remainingMinutes = remainingSeconds / 60
        var displayMinutes = "\(remainingMinutes)"
        var displaySeconds = "\(remainingSeconds - remainingMinutes * 60)"
        var isNegative = false
        if displaySeconds.first == "-" {
            isNegative = true
            displaySeconds.removeFirst()
        }
        if isNegative {
            displayMinutes = "-" + displayMinutes
        }
        if displaySeconds.count == 1 {
            displaySeconds = "0" + displaySeconds
        }
        return "\(displayMinutes):\(displaySeconds)"
    }
}
