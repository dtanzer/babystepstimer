#import "ViewController.h"

@import AVFoundation;

static const NSUInteger secondsInCycle = 120;

@interface ViewController ()
@property (nonatomic, strong) IBOutlet UILabel *timerLabel;
@property (nonatomic, strong) IBOutlet UIButton *resetButton;
@property (nonatomic, strong) IBOutlet UIButton *startStopButton;
@property (nonatomic, assign) BOOL isTimerRunning;
@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, assign) NSUInteger elapsedSeconds;
@property (nonatomic, strong) AVAudioPlayer *warningSoundPlayer;
@property (nonatomic, strong) AVAudioPlayer *failureSoundPlayer;
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.timerLabel.text = [self remainingTimeWithElapsedSeconds:0];
    self.resetButton.enabled = NO;
    NSString *warningSoundPath = [NSBundle.mainBundle pathForResource:@"2166__suburban-grilla__bowl-struck" ofType:@"wav"];
    self.warningSoundPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL fileURLWithPath:warningSoundPath] error:NULL];
    [self.warningSoundPlayer prepareToPlay];
    NSString *failureSoundPath = [NSBundle.mainBundle pathForResource:@"32304__acclivity__shipsbell" ofType:@"wav"];
    self.failureSoundPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:[NSURL fileURLWithPath:failureSoundPath] error:NULL];
    [self.failureSoundPlayer prepareToPlay];
}

- (IBAction)startStop
{
    if (!self.isTimerRunning)
    {
        self.isTimerRunning = YES;
        [self.startStopButton setTitle:@"Stop" forState:UIControlStateNormal];
        self.resetButton.enabled = YES;
        [self startTimer];
        self.view.backgroundColor = UIColor.whiteColor;
    }
    else
    {
        self.isTimerRunning = NO;
        [self.startStopButton setTitle:@"Start" forState:UIControlStateNormal];
        self.resetButton.enabled = NO;
        [self.timer invalidate];
        [self.warningSoundPlayer pause];
        self.warningSoundPlayer.currentTime = 0;
        [self.failureSoundPlayer pause];
        self.failureSoundPlayer.currentTime = 0;
    }
}

- (IBAction)reset
{
    self.timerLabel.text = [self remainingTimeWithElapsedSeconds:0];
    [self.timer invalidate];
    [self startTimer];
    self.view.backgroundColor = UIColor.greenColor;
    [self.warningSoundPlayer pause];
    self.warningSoundPlayer.currentTime = 0;
    [self.failureSoundPlayer pause];
    self.failureSoundPlayer.currentTime = 0;
}


- (void)startTimer
{
    self.timerLabel.text = [self remainingTimeWithElapsedSeconds:0];
    self.elapsedSeconds = 0;
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1 repeats:YES block:^(NSTimer *timer) {
        self.elapsedSeconds += 1;
        NSString *remainingTime = [self remainingTimeWithElapsedSeconds:self.elapsedSeconds];
        if ([remainingTime isEqualToString:@"0:10"])
        {
            [self.warningSoundPlayer play];
        }
        else if ([remainingTime isEqualToString:@"0:00"])
        {
            [self.failureSoundPlayer play];
            self.view.backgroundColor = UIColor.redColor;
        }
        else if ([remainingTime characterAtIndex:0] != '-')
        {
            self.view.backgroundColor = UIColor.whiteColor;
        }
        self.timerLabel.text = remainingTime;
    }];
}

- (NSString *)remainingTimeWithElapsedSeconds:(NSUInteger)elapsedSeconds
{
    NSInteger remainingSeconds = secondsInCycle - elapsedSeconds;
    NSInteger remainingMinutes = remainingSeconds / 60;
    NSMutableString *displayMinutes = [NSMutableString stringWithFormat:@"%li", remainingMinutes];
    NSMutableString *displaySeconds = [NSMutableString stringWithFormat:@"%li", remainingSeconds - remainingMinutes * 60];
    BOOL isNegative = NO;
    if ([displaySeconds characterAtIndex:0] == '-')
    {
        isNegative = YES;
        [displaySeconds deleteCharactersInRange:NSMakeRange(0, 1)];
    }
    if (isNegative)
    {
        [displayMinutes insertString:@"-" atIndex:0];
    }
    if (displaySeconds.length == 1)
    {
        [displaySeconds insertString:@"0" atIndex:0];
    }
    return [NSString stringWithFormat:@"%@:%@", displayMinutes, displaySeconds];
}

@end
